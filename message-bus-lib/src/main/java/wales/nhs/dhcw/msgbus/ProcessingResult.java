package wales.nhs.dhcw.msgbus;

import java.util.Optional;

public record ProcessingResult(
    boolean success,
    Optional<String> errorReason,
    Optional<Boolean> retry
){
    public static ProcessingResult successful() {
        return new ProcessingResult(true, Optional.empty(), Optional.empty());
    }

    public static ProcessingResult failed(String errorReason, boolean retry) {
        return new ProcessingResult(false, Optional.ofNullable(errorReason), Optional.ofNullable(retry));
    }
}
