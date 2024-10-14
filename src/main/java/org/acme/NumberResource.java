package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.Random;

@ApplicationScoped
@Path("/numbers")
public class NumberResource {

    @Channel("generated-number")
    Emitter<Integer> numberEmitter;

    private Random random = new Random();

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String triggerNumber() {
        int randomNumber = random.nextInt(100);
        long timestamp = System.currentTimeMillis();
        RandomNumber event = new RandomNumber(randomNumber, timestamp);
        
        numberEmitter.send(event);
        return "Sent: " + randomNumber + " at " + timestamp;
    }
}

