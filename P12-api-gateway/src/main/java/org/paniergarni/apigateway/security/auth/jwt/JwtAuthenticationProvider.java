package org.paniergarni.apigateway.security.auth.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.token.JwtAuthenticationToken;
import org.paniergarni.apigateway.security.token.JwtToken;
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
        UserContext context = UserContext.create(subject, Role.getListAuthorities(listRoles));
          
        return new JwtAuthenticationToken(context, context.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
