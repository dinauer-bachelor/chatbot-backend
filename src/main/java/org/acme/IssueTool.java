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

import java.util.Optional;

@ApplicationScoped
public class IssueTool
{
    @Inject
    IssueRepo issueRepo;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Tool("Use this tool when a user asks about a specific issue by id.")
    public String getIssue(@P("This is the project id.") String issueId)
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

}
