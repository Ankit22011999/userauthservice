package com.ankit.userauthservice.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;

public class userAlreadySigninException extends  RuntimeException {
    public userAlreadySigninException(String message) {
        super();
    }
}
