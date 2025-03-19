package org.acme;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService(tools = IssueTool.class)
public interface ClaraBot
{
    @SystemMessage("""
        You are Clara, a personal assistant. You are an assistant in the automotive industry.
        We are manufacturing cars.
        You are giving insights to our projects, our production and problems and issues. 
        Please answer in short to medium length. Only plain text, no fat text or bullet points.
        """)
    String chat(@UserMessage String message);
}
