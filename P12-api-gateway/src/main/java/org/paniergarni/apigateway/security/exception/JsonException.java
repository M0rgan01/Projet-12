package org.paniergarni.apigateway.security.exception;

import org.springframework.security.core.AuthenticationException;

public class JsonException extends AuthenticationException {
    public JsonException(String msg) {
        super(msg);
    }
}
