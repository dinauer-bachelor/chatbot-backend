package org.acme;

import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import dev.langchain4j.model.output.TokenUsage;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class ClaraChatModelListener implements ChatModelListener
{
    private static final Logger LOG = LoggerFactory.getLogger(ClaraChatModelListener.class);

    @Override
    public void onRequest(ChatModelRequestContext requestContext)
    {
        LOG.info("Request sent to OpenAI.");
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext)
    {
        TokenUsage tokenUsage = responseContext.chatResponse().tokenUsage();
        LOG.info("Response received from OpenAI. [Input Token used: {}, Output token used: {}]", tokenUsage.inputTokenCount(), tokenUsage.outputTokenCount());
    }
}
