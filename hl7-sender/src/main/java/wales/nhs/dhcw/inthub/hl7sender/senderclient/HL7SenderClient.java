package wales.nhs.dhcw.inthub.hl7sender.senderclient;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.app.Connection;
import ca.uhn.hl7v2.app.Initiator;
import ca.uhn.hl7v2.llp.LLPException;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.v251.message.ACK;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.parser.XMLParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wales.nhs.dhcw.inthub.hl7sender.AppConfig;
import wales.nhs.dhcw.msgbus.ProcessingResult;

import java.io.IOException;

public class HL7SenderClient implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(HL7SenderClient.class);

    private final Connection connection;
    private final PipeParser pipeParser;
    private final XMLParser xmlParser;
    private final Initiator initiator;

    public HL7SenderClient(HapiContext context, AppConfig config) throws HL7Exception {
        connection = context.newClient(
            config.receiverMllpHost(),
            config.receiverMllpPort(),
            config.useTlsForMLLP()
        );
        pipeParser = context.getPipeParser();
        xmlParser = context.getXMLParser();
        initiator = connection.getInitiator();
    }

    public ProcessingResult sendMessage(String message) {
        Message xmlParsedMessage;
        String pipeParsedMessageString;
        Message pipeParsedMessage;

        try {
            xmlParsedMessage = xmlParser.parse(message);
            pipeParsedMessageString = pipeParser.encode(xmlParsedMessage);
            pipeParsedMessage = pipeParser.parse(pipeParsedMessageString);
        } catch (HL7Exception e) {
            String error = e.getMessage();
            LOGGER.error(error);
            return ProcessingResult.failed(error, false);
        }

        Message response;
        try {
            response = initiator.sendAndReceive(pipeParsedMessage);
        } catch (HL7Exception | LLPException | IOException e) {
            String error = e.getMessage();
            LOGGER.error(error);
            return ProcessingResult.failed(error, true);
        }

        return getAckResult(response);
    }

    private static ProcessingResult getAckResult(Message response) {
        if (response instanceof ACK) {
            ACK ack = (ACK) response;
            String ackCode = ack.getMSA().getAcknowledgmentCode().getValue();
            LOGGER.debug("ACK Code: {}", ackCode);

            if ("AA".equals(ackCode) || "CA".equals(ackCode)) {
                LOGGER.debug("Valid ACK received.");
                return ProcessingResult.successful();
            } else {
                String error = "Negative ACK received: " + ackCode + " for: " + ack.getMSH().getMessageControlID();
                LOGGER.error(error);
                return ProcessingResult.failed(error, true);
            }
        } else {
            String error = "Received a non-ACK message";
            LOGGER.error(error);
            return ProcessingResult.failed(error, true);
        }
    }

    @Override
    public void close()  {
        connection.close();
    }

}
