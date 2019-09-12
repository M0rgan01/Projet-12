package org.paniergarni.apigateway.security.exception;

import org.springframework.security.core.AuthenticationException;

public class ProxyException extends AuthenticationException {

    public ProxyException(String msg) {
        super(msg);
    }

}
