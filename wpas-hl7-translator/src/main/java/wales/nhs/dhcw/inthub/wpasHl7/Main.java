package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.parser.Parser;
import ca.uhn.hl7v2.util.EncodedMessageComparator;
import com.azure.core.util.BinaryData;
import jakarta.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import wales.nhs.dhcw.inthub.wpasHl7.servicebus.MessageReceiverClient;
import wales.nhs.dhcw.inthub.wpasHl7.servicebus.MessageSenderClient;
import wales.nhs.dhcw.inthub.wpasHl7.servicebus.ServiceBusClientFactory;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final int MAX_BATCH_SIZE = 1000;
    private static volatile boolean PROCESSOR_RUNNING = true;
    private static WpasXmlParser parser;


    public static void main(String[] args) {
        AppConfig config = AppConfig.readEnvConfig();
        ServiceBusClientFactory factory = new ServiceBusClientFactory(config);
        DateTimeProvider dateTimeProvider = new DateTimeProvider();
        WpasHl7Translator wpasHl7Translator = new WpasHl7Translator(dateTimeProvider);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Shutting down the Transformer");
            PROCESSOR_RUNNING = false;
        }));

        try (MessageSenderClient senderClient = factory.createMessageSenderClient();
             MessageReceiverClient receiverClient = factory.createMessageReceiverClient()) {

            LOGGER.info("Transformation starts.");

            while (PROCESSOR_RUNNING) {
                receiverClient.receiveMessages(MAX_BATCH_SIZE, message -> {
                    var messageBody = message.getBody();
                    LOGGER.debug("Received message: {}", messageBody);
                    try {
                        parser = new WpasXmlParser();
                       var transformedObj= wpasHl7Translator.translate(parser.parseUTF8(message.getBody().toStream()));
                        HapiContext context = new DefaultHapiContext();
                        Parser parser = context.getXMLParser();
                        senderClient.sendMessage(BinaryData.fromObject(EncodedMessageComparator.standardizeXML(parser.encode(transformedObj))));
                        return true;
                    } catch (JAXBException | HL7Exception | SAXException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }
}
