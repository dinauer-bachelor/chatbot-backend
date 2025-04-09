package dev.dinauer;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class ClaraResource
{

    @Inject
    ClaraBot claraBot;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(Message message)
    {
        MessageContext context = message.context();
        if (context != null && context.user() != null && !context.user().isBlank())
        {
            return claraBot.get().chat("You are talking to " + context.user() + ". He asks or wants to know more about: " + message.text());
        }
        return claraBot.get().chat("The user asks or wants to know more about: " + message.text());
    }
}
