package org.acme.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.acme.persistence.entity.Issue;
import org.acme.persistence.repo.IssueRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentOfRequest
{
    private static final Logger LOG = LoggerFactory.getLogger(ContentOfRequest.class);

    @Inject
    IssueRepo issueRepo;

    @Tool("When a user asks about things that having nothing to do with automotive industry or manufacturing/production then use this tool. When the user asks any for a problem or incident without providing a issue key or project key. Use this tool.")
    public String content(@P("This parameter are the top key words of the request. Provide 5 keywords for short user inputs and up to 10 for longer inputs. No basic words like 'happened', 'faced', 'needed' etc. Please also pass synonyms for the keywords.") List<String> keywords) throws JsonProcessingException
    {
        LOG.info("Following keywords where identified: {}", keywords);
        String where = keywords.stream().map(keyword -> "description.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%' OR summary.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%'").collect(Collectors.joining(" OR "));
        List<Issue> issues = issueRepo.findByWhere(where);
        LOG.info("Found {} issues from keywords.", issues.size());
        return new ObjectMapper().writeValueAsString(issues);
    }
}
