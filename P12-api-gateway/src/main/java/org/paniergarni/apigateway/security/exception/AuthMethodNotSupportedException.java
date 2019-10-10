package org.paniergarni.apigateway.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author Pichat morgan
 *
 * 20 Juillet 2019
 */

public class AuthMethodNotSupportedException extends AuthenticationException {
    private static final long serialVersionUID = 3705043083010304496L;

    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
