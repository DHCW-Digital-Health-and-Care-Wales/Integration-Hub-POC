package wales.nhs.dhcw.inthub.validator.wpas.xml.validator;

import java.util.Optional;

public record ValidationResult(
    boolean success,
    Optional<String> errorReason
) { }
