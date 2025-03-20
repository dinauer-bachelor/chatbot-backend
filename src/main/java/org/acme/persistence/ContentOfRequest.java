package org.acme.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.repo.IssueRepo;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentOfRequest
{
    @Inject
    IssueRepo issueRepo;

    @Tool("When the user asks any for a problem or incident without providing a issue key or project key. Use this tool. Please filter out content that does not at all match the users needs.")
    public String content(@P("This parameter are the seven top key words of the request. Please also pass synonyms for the keywords.") List<String> keywords) throws JsonProcessingException
    {
        String where = keywords.stream().map(keyword -> "description.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%' OR summary.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%'").collect(Collectors.joining(" OR "));
        String result = new ObjectMapper().writeValueAsString(issueRepo.findByWhere(where));
        System.out.println(result);
        return result;
    }
}
