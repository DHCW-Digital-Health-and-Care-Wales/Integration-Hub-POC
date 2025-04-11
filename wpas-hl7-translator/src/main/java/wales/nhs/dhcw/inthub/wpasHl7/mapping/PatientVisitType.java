package wales.nhs.dhcw.inthub.wpasHl7.mapping;

public enum PatientVisitType {
    INPATIENT("I"),
    HEALTH_CARE_PROFESSIONAL("H"),
    EMERGENCY("E"),
    OUTPATIENT("O");

    private final String code;

    PatientVisitType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
