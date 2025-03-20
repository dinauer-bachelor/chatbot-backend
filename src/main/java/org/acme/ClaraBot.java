package org.acme;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import org.acme.persistence.ContentOfRequest;

@RegisterAiService(tools = {IssueTool.class, ProjectTool.class, CommentTool.class, ContentOfRequest.class})
public interface ClaraBot
{
    @SystemMessage("""
        You are Clara, a personal assistant. You are an assistant in the automotive industry.
        When the user is friendly and greets you, ask him how you can help him with the following:
        You are mainly but not only giving insights to our projects, our production and problems and issues and everything related to our company.
        Answer every question even if it has nothing to do with automotive industry. If you have no context or a short user message use the correct tool.
        Please answer in short to medium length. Only plain text, no fat text or bullet points.
        Please sometimes greet the user by his name. Please insert a new line after the greeting.
        
        We manufacture luxury sportscars and suvs. We have three plants in germany.
        """)
    String chat(@UserMessage String message);
}
