package org.acme;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.Jsonb;
import java.util.Map;

@ApplicationScoped
public class NumberConsumer {
    private static final Logger LOG = Logger.getLogger(NumberConsumer.class);
    private final Jsonb jsonb = JsonbBuilder.create();

    @Incoming("numbers")
    public void consume(String jsonString) {
        LOG.infof("Received raw message: %s", jsonString);
        try {
            Map<String, Object> numberEvent = jsonb.fromJson(jsonString, Map.class);
            LOG.infof("Deserialized message: %s", numberEvent);
            
            Integer number = ((Number) numberEvent.get("number")).intValue();
            Long timestamp = ((Number) numberEvent.get("timestamp")).longValue();
            LOG.infof("Received number: %d at timestamp: %d", number, timestamp);
        } catch (Exception e) {
            LOG.errorf("Error processing message: %s", e.getMessage());
            LOG.errorf("Received JSON: %s", jsonString);
        }
    }
}