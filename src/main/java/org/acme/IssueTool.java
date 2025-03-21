package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Issue;
import org.acme.persistence.repo.IssueRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class IssueTool
{
    private final Logger LOG = LoggerFactory.getLogger(IssueTool.class);

    @Inject
    IssueRepo issueRepo;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Tool("Use this tool when a user asks about a specific issue by id.")
    public String getIssue(@P("This is the issue key/id.") String issueId)
    {
        LOG.info(String.format("Looking for issue %s.", issueId));
        Optional<Issue> optionalIssue = issueRepo.findById(issueId);
        if(optionalIssue.isPresent())
        {
            LOG.info(String.format("Found one issue for key %s.", issueId));
            try
            {
                return OBJECT_MAPPER.writeValueAsString(optionalIssue.get());
            } catch (JsonProcessingException e) {
                return "I encountered an error. Sorry.";
            }
        } else
        {
            LOG.info(String.format("Found no issue %s.", issueId));
        }
        return "There is no information about this issue. Try another one.";
    }

    @Tool("""
        Use this tool when the user wants to know about the past for an issue or if the user indicates that he wants to know what changed. Use this tool when a user asks about new insights for an issue.
        Explain to the user what exactly changed between the different versions of the issue.
        """)
    public String getHistory(@P("This is the issue key/id.")  String issueKey)
    {
        LOG.info(String.format("Looking for history of issue %s.", issueKey));
        Optional<Map<ZonedDateTime, Issue>> history = issueRepo.findHistoryById(issueKey);
        if(history.isPresent())
        {
            LOG.info(String.format("Found history of one issue for key %s.", issueKey));
            try
            {
                return OBJECT_MAPPER.writeValueAsString(history.get());
            } catch (JsonProcessingException e)
            {
                return "I encountered an error.";
            }
        } else
        {
            LOG.info(String.format("No issue found for key %s.", issueKey));
        }
        return String.format("I cannot provide you with any information about issue %s", issueKey);
    }

    @Tool("Use this tool if the user asks for issue and mentions a reporter or/and assignee or/and project key and/or status. Pass a SQL Where clause without the where in front. Do not search by equals but by contains like this: '%input%'. If the user does not mention an attribute leave it out completely.")
    public String String(@P("This is the SQL where clause you identify from the users prompt. Attributes you can use are 'reporter.toLowerCase()', 'status.toLowerCase()', 'assignee.toLowerCase()' or 'project_key.toLowerCase()' use 'AND' or 'OR' to connect them") String where)
    {
        try
        {
            return OBJECT_MAPPER.writeValueAsString(issueRepo.findByWhere(where));
        } catch (JsonProcessingException e)
        {
            return "Sorry. I made a mistake when looking for issues. Please try again";
        }
    }
}
