package wales.nhs.dhcw.asbproducer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for the application.
 * This class contains the entry point of the application.
 */
public final class Main {

    /**
     * Logger instance for the Main class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    /**
     * The entry point of the application.
     * Prints a welcome message to the console.
     *
     * @param args the command-line arguments
     */
    public static void main(final String[] args) {
        LOGGER.info("Asb Producer - START");
        var producer = new MessageProducer(AppConfig.readEnvConfig());
        producer.produce();
        LOGGER.info("Asb Producer - END");
    }
}

