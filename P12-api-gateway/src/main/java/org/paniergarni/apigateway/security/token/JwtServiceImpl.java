package org.paniergarni.apigateway.security.token;


import feign.FeignException;
import feign.RetryableException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.SecurityConstants;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.exception.ProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author pichat morgan
 *
 * 20 Juillet 2019
 */
@Service
public class JwtServiceImpl implements JwtService {

    @Autowired
    private UserProxy userProxy;

    @Override
    public String createAuthToken(UserContext userContext) {
        if (userContext.getUsername() == null || userContext.getUsername().isEmpty())
            throw new IllegalArgumentException("jwt.auth.username.null");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("jwt.auth.authorities.null");

        // construction du Json web token
        String jwt = Jwts.builder().setSubject(userContext.getUsername()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_AUTH_TOKEN)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(SecurityConstants.AUTHORITIES_PREFIX, userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList())) // ajout personnalisé --> on ajoute les roles
                .compact(); // construction du token
        return jwt;
    }

    @Override
    public String createRefreshToken(UserContext userContext) {

        if (userContext.getUsername() == null || userContext.getUsername().isEmpty())
            throw new IllegalArgumentException("jwt.refresh.username.null");

        // construction du Json web token
        String jwt = Jwts.builder().setSubject(userContext.getUsername()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME_REFRESH_TOKEN)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, SecurityConstants.SECRET) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(SecurityConstants.REFRESH_ACTIVE_PREFIX, true) // ajout de vérification pour le rafraichissement
                .compact(); // construction du token
        return jwt;
    }

    @Override
    public UserContext validateRefreshToken(JwtToken token) {

        // vérification du token
        Jws<Claims> claims = token.parseClaims(token.getToken());

        // on recupere le contact pour comparaison
        User contact;
        try {
            contact = userProxy.findByUserName(claims.getBody().getSubject());
        } catch (FeignException e1) {
            if (e1 instanceof RetryableException)
                throw new AuthenticationServiceException("internal.error");
            throw new UsernameNotFoundException("user.not.found");
        } catch (Exception e) {
            throw new ProxyException("internal.error");
        }

        // si le contact est toujours bon, alors le token est toujours valide
        if (contact.isActive() != (boolean) claims.getBody().get(SecurityConstants.REFRESH_ACTIVE_PREFIX))
            throw new DisabledException("contact.not.active");

        return UserContext.create(claims.getBody().getSubject(), Role.getListAuthorities(contact.getRoles()));
    }

    @Override
    public String extract(String header) {

        if (header == null || header.isEmpty()) {
            throw new AuthenticationServiceException("authorization.header.blank");
        }

        return header.substring(SecurityConstants.TOKEN_PREFIX.length(), header.length());
    }

}
