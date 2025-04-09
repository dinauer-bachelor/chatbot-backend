package org.acme;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ChatRequestParameters;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.Response;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class ClaraChatModel
{
    private ChatLanguageModel model;

    @ConfigProperty(name = "quarkus.langchain4j.openai.api-key")
    String openAiKey;

    @ConfigProperty(name = "quarkus.langchain4j.openai.chat-model.model-name")
    String modelName;

    @Inject
    ClaraChatModelListener listener;

    @PostConstruct
    void init()
    {
        model = OpenAiChatModel.builder()
            .apiKey(openAiKey)
            .modelName(modelName)
            .listeners(List.of(listener))
            .build();
    }

    public ChatLanguageModel get()
    {
        return model;
    }
}
