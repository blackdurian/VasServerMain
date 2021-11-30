package com.fyp.vasclinicserver.exceptions;

public class NotificationException extends RuntimeException{
    public NotificationException(String exMessage, Exception exception) {
        super(exMessage, exception);
    }

}
