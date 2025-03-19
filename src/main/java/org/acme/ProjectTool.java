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
            try {
                return OBJECT_MAPPER.writeValueAsString(optionalProject.get());
            } catch (JsonProcessingException e) {
                return "I encountered an error. Sorry.";
            }
        }
        return "There is no information about this project. Try another one.";
    }
}
