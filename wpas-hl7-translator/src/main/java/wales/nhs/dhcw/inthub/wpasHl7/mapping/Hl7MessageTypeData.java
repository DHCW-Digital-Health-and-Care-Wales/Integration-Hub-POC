package wales.nhs.dhcw.inthub.wpasHl7.mapping;

public record Hl7MessageTypeData(
        String messageTypeCode,
        String triggerEvent,
        String messageStructure
) {
}