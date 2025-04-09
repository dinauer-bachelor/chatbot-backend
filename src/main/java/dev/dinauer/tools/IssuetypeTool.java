package dev.dinauer.tools;

import dev.langchain4j.agent.tool.Tool;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.AbstractMap;
import java.util.Map;

@ApplicationScoped
public class IssuetypeTool
{
    @Tool("Use this tool if the user wants to know what an issuetype means")
    public String issuetypeMeaning()
    {
        System.out.println("Looking for information on issuetypes.");
        return Map.ofEntries(
                new AbstractMap.SimpleEntry<>("bug", "indicates that the issue describes a problem or an incident that occured due to a problem and needs a fix"),
                new AbstractMap.SimpleEntry<>("task", "indicates that this issue is about a piece of work that needs to be done."),
                new AbstractMap.SimpleEntry<>("story", "means that this is a requirement we need to implement.")
        ).toString();
    }
}
