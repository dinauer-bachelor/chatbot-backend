package dev.dinauer.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.dinauer.persistence.entity.Issue;
import dev.dinauer.persistence.repo.IssueRepo;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ContentOfRequestTool
{
    private static final Logger LOG = LoggerFactory.getLogger(ContentOfRequestTool.class);

    @Inject
    IssueRepo issueRepo;

    @Tool("""
            When you have no idea what the user wants use this tool.
            When you have no context or a really short user message, use this tool.
            When the user asks any for a problem or incident without providing a issue key or project key. Use this tool.
            Filter the response and only provide content in the response that is relevant to what the user wants.
            Do not trust that this tool gives you only relevant information. Do not show issues to the user that do not match we that want to hear.
            You should generally sum up and tell the user how many issues you found are relevant to him. If there are too much issues tell the user that you only provide some of them.
            """)
    public String content(@P("This parameter are the top key words of the request. Dont just use any keywords from the request. Provide 4 keywords for short user inputs and up to 8 for longer inputs. No basic words like 'happened', 'faced', 'needed' etc. Pass them in different languages too. German and English. Please also pass synonyms for the keywords.") List<String> keywords, @P("If you think a few issues are enough or you think a limit is a good idea use this param. Choose a rather higher limit than too low. pass null if no limit.") Integer limit) throws JsonProcessingException
    {
        List<String> normalizedKeywords = new LinkedList<>();
        for (String keyword : keywords)
        {
            normalizedKeywords.addAll(Arrays.stream(keyword.split("\\s+")).toList());
        }
        LOG.info("Tool used: findByKeywords. With limit . Following keywords where identified: {}", normalizedKeywords);
        String where = normalizedKeywords.stream().map(keyword -> "description.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%' OR summary.toLowerCase() LIKE '%" + keyword.toLowerCase() + "%'").collect(Collectors.joining(" OR "));
        List<Issue> issues = issueRepo.findByWhere(where, limit);
        LOG.info("Found {} issues from keywords.", issues.size());
        return new ObjectMapper().writeValueAsString(issues);
    }
}
