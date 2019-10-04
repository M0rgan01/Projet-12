package org.paniergarni.apigateway.security.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.security.token.JwtAuthenticationToken;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Processus d'authentification par JWT 
 * 
 * 
 * @author Pichat morgan
 *
 * 20 Juillet 2019
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationProcessingFilter.class);
    @Value("${jwt.prefix.authorities}")
    private String authoritiesPrefix;
    @Value("${jwt.secret}")
    private String secret;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    	
        JwtToken jwtToken = (JwtToken) authentication.getCredentials();

        //on récupère les claimes, tout en vérifiant le token
        Jws<Claims> jwsClaims = jwtToken.parseClaims(jwtToken.getToken(), secret);
        
        // récupère le sujet (username)
        String subject = jwsClaims.getBody().getSubject();
        
        @SuppressWarnings("unchecked")
		List<String> listRoles = jwsClaims.getBody().get(authoritiesPrefix, List.class);

        //création d'un utilisateur grace au nom à la liste de role contenu dans le token
        User context = new User();
        context.setUserName(subject);
        context.setAuthorities(Role.getListAuthorities(listRoles));
        logger.debug("Success authentication for userName " + subject);
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
