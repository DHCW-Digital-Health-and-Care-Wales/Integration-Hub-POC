package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.Parser;

public class Hl7Encoder {
    HapiContext context;

    public Hl7Encoder() {
        context = new DefaultHapiContext();
    }

    public String encode(Message message) throws HL7Exception {
        Parser parser = context.getXMLParser();
        return parser.encode(message);
    }
}
