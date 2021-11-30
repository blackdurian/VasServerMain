package com.fyp.vasclinicserver.exceptions;

public class VasException extends RuntimeException {
    public VasException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

    public VasException(String exMessage) {
        super(exMessage);
    }
}
