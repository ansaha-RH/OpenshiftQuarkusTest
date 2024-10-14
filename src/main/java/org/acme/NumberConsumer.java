package org.acme;

import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;

@ApplicationScoped
public class NumberConsumer {

    private static final Logger LOG = Logger.getLogger(NumberConsumer.class);

    @Incoming("numbers")
    public void consume(Map<String, Object> numberEvent) {
        Integer number = (Integer) numberEvent.get("number");
        Long timestamp = (Long) numberEvent.get("timestamp");
        
        LOG.infof("Received number: %d at timestamp: %d", number, timestamp);
    }
}

/* test 3*/