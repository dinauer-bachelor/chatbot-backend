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

import javax.swing.text.html.Option;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
public class IssueTool
{
    @Inject
    IssueRepo issueRepo;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Tool("Use this tool when a user asks about a specific issue by id.")
    public String getIssue(@P("This is the issue key/id.") String issueId)
    {
        Optional<Issue> optionalIssue = issueRepo.findById(issueId);
        if(optionalIssue.isPresent()) {
            try {
                return OBJECT_MAPPER.writeValueAsString(optionalIssue.get());
            } catch (JsonProcessingException e) {
                return "I encountered an error. Sorry.";
            }
        }
        return "There is no information about this issue. Try another one.";
    }

    @Tool("""
        Use this tool when the user wants to know about the past for an issue or if the user indicates that he wants to know what changed. Use this tool when a user asks about new insights for an issue.
        Explain to the user what exactly changed between the different versions of the issue.
        """)
    public String getHistory(@P("This is the issue key/id.")  String issueKey)
    {
        Optional<Map<ZonedDateTime, Issue>> history = issueRepo.findHistoryById(issueKey);
        if(history.isPresent())
        {
            try
            {
                String result = OBJECT_MAPPER.writeValueAsString(history.get());
                System.out.println(result);
                return result;
            } catch (JsonProcessingException e)
            {
                return "I encountered an error.";
            }
        }
        return String.format("I cannot provide you with any information about issue %s", issueKey);
    }
}
