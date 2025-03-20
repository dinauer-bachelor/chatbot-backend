package org.acme;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Project;
import org.acme.persistence.repo.ProjectRepo;

import java.util.Optional;

@ApplicationScoped
public class ProjectTool
{
    @Inject
    ProjectRepo projectRepo;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().registerModule(new JavaTimeModule());

    @Tool("Use this tool when a user asks about a specific project by key or id.")
    public String getProject(@P("This is the project key.") String key)
    {
        Optional<Project> optionalProject = projectRepo.findById(key);
        if(optionalProject.isPresent())
        {
            try
            {
                return OBJECT_MAPPER.writeValueAsString(optionalProject.get());
            } catch (JsonProcessingException e)
            {
                return "I encountered an error. Sorry.";
            }
        }
        return "There is no information about this project. Try another one.";
    }

    @Tool("""
        Use this tool when a user wants to know about our projects in general.
        Use this tool when you want to find a project if you have no key.
        Inform the user by explicitly mentioning their keys in project name.
        Count them like 1. 2. etc. but just with plain text. Add line breaks after each project.
    """)
    public String getProjects()
    {
        try
        {
            return OBJECT_MAPPER.writeValueAsString(projectRepo.findAll());
        } catch (JsonProcessingException e)
        {
            return "I encountered an error. Sorry.";
        }
    }
}