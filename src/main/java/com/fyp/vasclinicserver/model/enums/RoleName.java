package com.fyp.vasclinicserver.model.enums;

public enum RoleName {
    ROLE_RECIPIENT,
    ROLE_CLINIC_ADMIN,
    ROLE_CLINIC_DOCTOR,
    ROLE_GOVT_AGENCY;

    public String getLabel(){
        switch(this) {
            case ROLE_RECIPIENT:
                return "Recipient";
            case ROLE_CLINIC_ADMIN:
                return "Clinic Admin";
            case ROLE_CLINIC_DOCTOR:
                return "Clinic Doctor";
            case ROLE_GOVT_AGENCY:
                return "Government Agency";
            default:
                return "Undefined Role";
        }
    }
}

