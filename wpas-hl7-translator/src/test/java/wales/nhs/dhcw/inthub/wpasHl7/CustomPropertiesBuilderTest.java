package wales.nhs.dhcw.inthub.wpasHl7;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.AbstractMessage;
import ca.uhn.hl7v2.model.v251.message.ADT_A01;
import ca.uhn.hl7v2.model.v251.message.ADT_A05;
import ca.uhn.hl7v2.model.v251.segment.MSH;
import ca.uhn.hl7v2.model.v251.segment.PV1;
import org.junit.jupiter.api.Test;
import wales.nhs.dhcw.inthub.wpasHl7.mapping.HL7Constants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;


class CustomPropertiesBuilderTest {

    private CustomPropertiesBuilder builder = new CustomPropertiesBuilder();

    @Test
    void routingPropertiesAreSet() throws Exception {
        // Arrange
        var messageTypeCode = "ADT";
        var messageTypeEvent = "A01";
        var sendingApplication = "109";
        var specialty = "Spec";

        var hl7Message = new ADT_A01();
        buildHl7Message(hl7Message, messageTypeCode, messageTypeEvent, sendingApplication, specialty);

        // Act
        var result = builder.buildCustomProperties(hl7Message);

        // Assert
        assertEquals("ADT^A01" ,result.get(CustomPropertiesBuilder.MESSAGE_TYPE_PROPERTY));
        assertEquals(sendingApplication, result.get(CustomPropertiesBuilder.SENDING_APPLICATION_PROPERTY));
        assertEquals(specialty, result.get(CustomPropertiesBuilder.SPECIALTY_PROPERTY));
    }

    @Test
    void routingPropertiesAreSetWithoutSpecialty() throws Exception {
        // Arrange
        var messageTypeCode = "ADT";
        var messageTypeEvent = "A01";
        var sendingApplication = "109";

        var hl7Message = new ADT_A05();
        buildHl7Message(hl7Message, messageTypeCode, messageTypeEvent, sendingApplication, null);

        // Act
        var result = builder.buildCustomProperties(hl7Message);

        // Assert
        assertEquals("ADT^A01" ,result.get(CustomPropertiesBuilder.MESSAGE_TYPE_PROPERTY));
        assertEquals(sendingApplication, result.get(CustomPropertiesBuilder.SENDING_APPLICATION_PROPERTY));
        assertFalse(result.containsKey(CustomPropertiesBuilder.SPECIALTY_PROPERTY));
    }

    private static void buildHl7Message(AbstractMessage hl7Message, String messageTypeCode, String messageTypeEvent, String sendingApplication, String specialty) throws HL7Exception {
        var msh = (MSH)hl7Message.get("MSH");
        msh.getFieldSeparator().setValue(HL7Constants.PIPE_LINE);
        msh.getEncodingCharacters().setValue(HL7Constants.ENCODING_CHAR);
        msh.getMsh9_MessageType().getMsg1_MessageCode().setValue(messageTypeCode);
        msh.getMsh9_MessageType().getMsg2_TriggerEvent().setValue(messageTypeEvent);
        msh.getMsh3_SendingApplication().getNamespaceID().setValue(sendingApplication);

        if (specialty != null) {
            var pv1 = (PV1)hl7Message.get("PV1");
            pv1.getPv110_HospitalService().setValue(specialty);
        }
    }
}