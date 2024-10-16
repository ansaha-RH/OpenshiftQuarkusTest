package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Path("/numbers")
public class NumberResource {

    private static final Logger LOG = Logger.getLogger(NumberResource.class);

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

        try {
            numberEmitter.send(event);
            LOG.infof("Sent: %s", event);
            return "Sent: " + randomNumber + " at " + timestamp;
        } catch (Exception e) {
            LOG.errorf("Error sending message: %s", e.getMessage());
            return "Error sending message";
        }
    }
}

/*test1 */