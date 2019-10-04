package org.paniergarni.apigateway.security.auth.handler;


import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.security.token.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 
 * Processus de succès de connection personnalisé
 * 
 * @author Pichat morgan
 *
 * 20 Juillet 2019
 * 
 */
@Component
public class LoginAwareAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;

    @Autowired
    public LoginAwareAuthenticationSuccessHandler(final JwtService jwtService) {
        this.jwtService = jwtService;
    }
    @Value("${jwt.prefix}")
    private String tokenPrefix;
    @Value("${jwt.header.token.auth}")
    private String headerAuth;
    @Value("${jwt.header.token.refresh}")
    private String headerRefresh;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        User userContext = (User) authentication.getPrincipal();
        //création des token
        String accessToken = jwtService.createAuthToken(userContext);
        String refreshToken = jwtService.createRefreshToken(userContext);
        
        //on met en place le code de la réponse
        response.setStatus(HttpStatus.OK.value());
           
       //on ajoute le token d'auth et le token de refresh à l'en-tête de la réponse
      	response.addHeader(headerAuth, tokenPrefix + accessToken);
      	response.addHeader(headerRefresh, tokenPrefix + refreshToken);
        clearAuthenticationAttributes(request);
        
    }

    /**
     * Removes temporary authentication-related data which may have been stored
     * in the session during the authentication process..
     * 
     */
    protected final void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            return;
        }

        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
