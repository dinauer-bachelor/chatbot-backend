package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.PathParam;
import org.acme.persistence.entity.Comment;
import org.acme.persistence.repo.CommentRepo;

import java.util.List;

@ApplicationScoped
public class CommentTool
{
    @Inject
    CommentRepo commentRepo;

    @Tool("Use this tool when a user provides you with a issue id. Use this tool if a user asks for comments of a specific issue by key.")
    public String getCommentsByIssueKey(@P("This is the issue key the user wants the comments for") String issueKey)
    {
        List<Comment> comments = commentRepo.getLatestCommentsByIssueKey(issueKey);
        System.out.println("Kommentare: " + comments);
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
