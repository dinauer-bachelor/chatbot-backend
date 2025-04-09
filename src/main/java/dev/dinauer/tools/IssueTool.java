package dev.dinauer.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.dinauer.ClaraBotInterface;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.persistence.entity.SimilarIssue;
import dev.dinauer.persistence.repo.IssueRepo;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.*;

@ApplicationScoped
public class IssueTool
{
    private final Logger LOG = LoggerFactory.getLogger(IssueTool.class);

    @Inject
    IssueRepo issueRepo;

    @Inject
    ClaraBotInterface claraBot;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Tool("Use this tool when a user asks about a specific issue by id.")
    public String getIssue(@P("This is the issue key/id.") String issueId)
    {
        LOG.info(String.format("Tool used: getIssueByKey. Issue id is %s.", issueId));
        Optional<Issue> optionalIssue = issueRepo.findById(issueId);
        if (optionalIssue.isPresent())
        {
            LOG.info(String.format("Found one issue for key %s.", issueId));
            try
            {
                return OBJECT_MAPPER.writeValueAsString(optionalIssue.get());
            }
            catch (JsonProcessingException e)
            {
                return "I encountered an error. Sorry.";
            }
        }
        else
        {
            LOG.info(String.format("Found no issue %s.", issueId));
        }
        return "There is no information about this issue. Try another one.";
    }

    @Tool("""
            Use this tool if you think that the user wants to know what happened over time. Use it when the user requests a timeline or changelog etc.
            Use this tool if the user requests detailed information about something.
            Explain to the user what exactly changed between the different versions of the issue.
            """)
    public String getHistory(@P("This is the issue key/id.") String issueKey)
    {
        LOG.info(String.format("Tool used: getHistory. Issue id is %s.", issueKey));
        Optional<Map<ZonedDateTime, Issue>> history = issueRepo.findHistoryById(issueKey);
        if (history.isPresent())
        {
            LOG.info(String.format("Found history of one issue for key %s.", issueKey));
            try
            {
                return OBJECT_MAPPER.writeValueAsString(history.get());
            }
            catch (JsonProcessingException e)
            {
                return "I encountered an error.";
            }
        }
        else
        {
            LOG.info(String.format("No issue found for key %s.", issueKey));
        }
        return String.format("I cannot provide you with any information about issue %s", issueKey);
    }

    @Tool("Use this tool if the user asks for issue and mentions a reporter or/and assignee or/and project key. Pass a SQL Where clause without the where in front. Always format your conditions like this: attribute LIKE '%input%'. If the user does not mention an attribute leave it out completely.")
    public String String(@P("This is the SQL where clause you identify from the users prompt. Never use status as a condition or attribute to filter by here. Concat with OR. Attributes you can use are 'reporter.toLowerCase()', 'assignee.toLowerCase()' or 'project_key.toLowerCase()' use 'AND' or 'OR' to connect them.") String where,
                         @P("The limit of issues you want to request. Use this when you think its appropriate. Choose an appropriate limit, rather higher than too low. Choose null if no limit.") Integer limit,
                         @P("This is the status or the state requested issues need to be in. Use null if the user does not filter for status") String status)
    {
        if (status != null)
        {
            List<String> synonymsForStatus = Arrays.stream(claraBot.findSynonyms(status).split(",")).map(String::trim).toList();
            ;
            where = where + " AND (" + whereClauseFromConditions("status", synonymsForStatus) + ")";
        }

        LOG.info("Tool used: getByFields with limit {}", limit);
        try
        {
            List<Issue> result = issueRepo.findByWhere(where, limit);
            return "Found " + result.size() + " relevant issues: " + OBJECT_MAPPER.writeValueAsString(result);
        }
        catch (JsonProcessingException e)
        {
            return "Sorry. I made a mistake when looking for issues. Please try again";
        }
    }

    @Tool("Use this tool if the user asks for similar or equal issues.")
    public String getSimilarIssues(@P("The issue key") String key)
    {
        LOG.info("Tool used: getSimilarIssues for key {}", key);
        try
        {
            Optional<Issue> issue = issueRepo.findById(key);
            List<SimilarIssue> similarIssues = issueRepo.findSimilarByKey(key);
            if (!similarIssues.isEmpty() && issue.isPresent())
            {
                return "The following issue " + OBJECT_MAPPER.writeValueAsString(issue.get()) + " was compared to similar ones. 'similarity' marks how close they are. Those are: " + OBJECT_MAPPER.writeValueAsString(similarIssues);
            }
            return String.format("There are no issues similar to %s", key);
        }
        catch (NoSuchElementException e)
        {
            return "Sorry, this project key does not exist";
        }
        catch (JsonProcessingException e)
        {
            return "Sorry. I made a mistake when looking for issues. Please try again";
        }
    }

    private String whereClauseFromConditions(String attribute, List<String> values)
    {
        List<String> status = values.stream().map(item -> attribute + ".toLowerCase() = '" + item.toLowerCase() + "'").toList();
        return String.join(" OR ", status);
    }
}
