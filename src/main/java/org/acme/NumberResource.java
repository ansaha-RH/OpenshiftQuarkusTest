package org.acme;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.logging.Logger;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaSerializer;
import io.apicurio.registry.rest.client.RegistryClientFactory;
import io.apicurio.registry.rest.v2.RegistryClient;

import java.util.Random;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
@Path("/numbers")
public class NumberResource {

    private static final Logger LOG = Logger.getLogger(NumberResource.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonSchemaKafkaSerializer<Map<String, Object>> serializer;

    @Channel("generated-number")
    Emitter<Map<String, Object>> numberEmitter;

    private Random random = new Random();

    public NumberResource() {
        RegistryClient client = RegistryClientFactory.create("http://apicurioregistry-mem.kafka-apicurio-test.router-default.apps.cluster-945vs.945vs.sandbox191.opentlc.com/");
        serializer = new JsonSchemaKafkaSerializer<>(client, objectMapper);
    }

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
