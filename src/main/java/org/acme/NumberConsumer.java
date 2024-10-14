import org.eclipse.microprofile.reactive.messaging.Incoming;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.logging.Logger;

@ApplicationScoped
public class NumberConsumer {
    
    private static final Logger LOG = Logger.getLogger(NumberConsumer.class);

    @Incoming("numbers")
    public void consume(Integer number, Long timestamp) {
        LOG.infof("Received number: %d at %d", number, timestamp);
    }
}

/*test #6 */