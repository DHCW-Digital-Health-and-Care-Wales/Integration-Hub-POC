package wales.nhs.dhcw.inthub.validator.logging;

import java.io.InputStream;

public record Event(
    EventType eventType,
    long eventDateTime,
    String eventId,
    InputStream eventMessage,
    String eventInformation,
    String eventProcess
) {}
