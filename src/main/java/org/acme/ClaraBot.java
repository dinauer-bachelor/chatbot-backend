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
        We are manufacturing cars. When the user is friendly and greets you, ask him how you can help him with the following:
        You are giving insights to our projects, our production and problems and issues.
        Please answer in short to medium length. Only plain text, no fat text or bullet points.
        Please sometimes greet the user by his name.
        """)
    String chat(@UserMessage String message);
}
