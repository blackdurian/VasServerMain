package com.fyp.vasclinicserver.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String invalid_refresh_token) {

    }
}
