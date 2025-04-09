package org.acme.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Comment;
import org.acme.persistence.repo.CommentRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@ApplicationScoped
public class CommentTool
{
    private static final Logger LOG = LoggerFactory.getLogger(CommentTool.class);

    @Inject
    CommentRepo commentRepo;

    @Tool("Use this tool when a user provides you with a issue id. Use this tool if a user asks for comments of a specific issue by key.")
    public String getCommentsByIssueKey(@P("This is the issue key the user wants the comments for") String issueKey)
    {
        LOG.info("Looking for comments for issue {}", issueKey);
        List<Comment> comments = commentRepo.getLatestCommentsByIssueKey(issueKey);
        LOG.info("Found {} comment(s) for issue {}", comments.size(), issueKey);
        if(!comments.isEmpty())
        {
            try
            {
                return new ObjectMapper().writeValueAsString(comments);
            } catch (JsonProcessingException e)
            {
                throw new RuntimeException(e);
            }
        }
        return "No comments found";
    }
}
