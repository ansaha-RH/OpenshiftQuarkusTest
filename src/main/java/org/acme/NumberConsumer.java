package org.acme;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;
import jakarta.enterprise.context.ApplicationScoped;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.apicurio.registry.serde.jsonschema.JsonSchemaKafkaDeserializer;
import io.apicurio.registry.rest.client.RegistryClientFactory;
import io.apicurio.registry.rest.v2.RegistryClient;

import java.util.Map;

@ApplicationScoped
public class NumberConsumer {
    private static final Logger LOG = Logger.getLogger(NumberConsumer.class);
    private final JsonSchemaKafkaDeserializer<Map<String, Object>> deserializer;

    public NumberConsumer() {
        RegistryClient client = RegistryClientFactory.create("http://apicurioregistry-mem.kafka-apicurio-test.router-default.apps.cluster-945vs.945vs.sandbox191.opentlc.com/");
        ObjectMapper objectMapper = new ObjectMapper();
        deserializer = new JsonSchemaKafkaDeserializer<>(client, objectMapper);
    }

    @Incoming("numbers")
    public void consume(Record<String, byte[]> record) {
        try {
            Map<String, Object> numberEvent = deserializer.deserialize(null, record.value());
            Integer number = (Integer) numberEvent.get("number");
            Long timestamp = (Long) numberEvent.get("timestamp");
            LOG.infof("Received number: %d at timestamp: %d", number, timestamp);
        } catch (Exception e) {
            LOG.errorf("Error processing message: %s", e.getMessage());
        }
    }
}