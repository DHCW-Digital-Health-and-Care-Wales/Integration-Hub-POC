package wales.nhs.dhcw.asblistener;

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
        LOGGER.info("Asb Listener - START");
        var listener = new LoggingListener(AsbConfig.readEnvConfig());
        listener.listen();
        LOGGER.info("Asb Listener - END");
    }
}

