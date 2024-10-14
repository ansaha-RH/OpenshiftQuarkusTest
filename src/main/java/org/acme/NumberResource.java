package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Path("/numbers")
public class NumberResource {
    @Channel("generated-number")
    Emitter<Map<String, Object>> numberEmitter;

    private Random random = new Random();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String triggerNumber() {
        int randomNumber = random.nextInt(100);
        long timestamp = System.currentTimeMillis();
        
        Map<String, Object> event = new HashMap<>();
        event.put("number", randomNumber);
        event.put("timestamp", timestamp);
        
        numberEmitter.send(event);
        return "Sent: " + randomNumber + " at " + timestamp;
    }
}