package wales.nhs.dhcw.msgbus;

import java.util.Optional;

public record ProcessingResult(
    boolean success,
    Optional<String> errorReason
){
    public static ProcessingResult successful() {
        return new ProcessingResult(true, Optional.empty());
    }

    public static ProcessingResult failed(String errorReason) {
        return new ProcessingResult(false, Optional.ofNullable(errorReason));
    }
}