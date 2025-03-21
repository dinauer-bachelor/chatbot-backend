package org.acme;

import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class ClaraResource {

    @Inject
    ClaraBot claraBot;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(Message message) {
        if(message.context() != null && message.context().user() != null && !message.context().user().isBlank()) {
            return claraBot.chat("You are talking to " + message.context().user() + ". He asks or wants to know more about: " + message);
        }
        return claraBot.chat("The user asks or wants to know more about: " + message.text());
    }
}
