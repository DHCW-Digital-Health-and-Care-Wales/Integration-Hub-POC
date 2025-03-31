package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PV1;

import java.util.HashMap;
import java.util.Set;

public class CustomPropertiesBuilder {

    public static final String MESSAGE_TYPE_PROPERTY = "hl7MessageType";
    public static final String SENDING_APPLICATION_PROPERTY = "hl7SendingApp";
    public static final String SPECIALTY_PROPERTY = "hl7Specialty";

    private static final String PATIENT_VISIT_SEGMENT = "PV1";

    public HashMap<String, String> buildCustomProperties(AbstractMessage hl7Data) throws HL7Exception {
        MSH msh = (MSH) hl7Data.get("MSH");

        var customProperties = new HashMap<String, String>();
        var messageType = msh.getMsh9_MessageType().getMsg1_MessageCode() + "^"
                + msh.getMsh9_MessageType().getMsg2_TriggerEvent();
        customProperties.put(MESSAGE_TYPE_PROPERTY, messageType);
        customProperties.put(SENDING_APPLICATION_PROPERTY,
                msh.getMsh3_SendingApplication().getNamespaceID().getValue()
        );
        if (messageContains(hl7Data, PATIENT_VISIT_SEGMENT)) {
            var pv1 = (PV1)hl7Data.get("PV1");
            String specialty = pv1.getPv110_HospitalService().getValue();
            if (specialty != null) {
                customProperties.put(SPECIALTY_PROPERTY, specialty);
            }
        }
        return customProperties;
    }

    private boolean messageContains(AbstractMessage message, String segmentName) {
        return Set.of(message.getNames()).contains(segmentName);
    }
}
