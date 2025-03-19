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
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(String message) {
        return claraBot.chat(message);
    }
}
