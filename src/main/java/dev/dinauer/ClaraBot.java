package dev.dinauer;

import dev.dinauer.tools.*;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class ClaraBot
{
    private ClaraBotInterface claraBotInterface;

    @Inject
    ClaraChatModel model;

    @Inject
    ContentOfRequestTool contentOfRequestTool;

    @Inject
    ProjectTool projectTool;

    @Inject
    IssueTool issueTool;

    @Inject
    IssuetypeTool issuetypeTool;

    @Inject
    CommentTool commentTool;

    @PostConstruct
    public void init()
    {
        List<Object> tools = List.of(contentOfRequestTool, projectTool, issueTool, issuetypeTool, commentTool);
        claraBotInterface = AiServices.builder(ClaraBotInterface.class)
                .chatLanguageModel(model.get())
                .tools(tools)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(5))
                .build();
    }

    public ClaraBotInterface get()
    {
        return claraBotInterface;
    }
}
