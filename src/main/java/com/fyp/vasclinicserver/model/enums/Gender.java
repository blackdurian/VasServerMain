package com.fyp.vasclinicserver.model.enums;

public enum Gender {
    M, F, UNKNOWN;

    public String getLabel(){
        switch(this) {
            case M:
                return "Male";
            case F:
                return "Female";
            case UNKNOWN:
            default:
                return "Unknown";
        }
    }
}
