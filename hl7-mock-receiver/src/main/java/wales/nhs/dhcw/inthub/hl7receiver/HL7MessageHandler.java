package wales.nhs.dhcw.inthub.hl7receiver;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.protocol.ReceivingApplication;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.msgbus.MessageSenderClient;

import java.io.IOException;
import java.util.Map;

public class HL7MessageHandler implements ReceivingApplication
{
    private final PipeParser pipeParser;
    private final MessageSenderClient messageSenderClient;
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(HL7Receiver.class);

    public HL7MessageHandler(HapiContext context, MessageSenderClient messageSenderClient) {
        this.pipeParser = context.getPipeParser();
        this.messageSenderClient = messageSenderClient;
    }

    @Override
    public Message processMessage(Message message, Map<String, Object> metadata) throws  HL7Exception {
        String encodedMessage = pipeParser.encode(message);
        LOGGER.info("Received message: {}", encodedMessage);
        try {
            messageSenderClient.sendMessage(encodedMessage);
            return message.generateACK();
        } catch (IOException e) {
            throw new HL7Exception(e);
        }
    }

    @Override
    public boolean canProcess(Message theMessage) {
        return true;
    }

}
