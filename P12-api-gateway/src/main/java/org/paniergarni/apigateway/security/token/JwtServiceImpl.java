package org.paniergarni.apigateway.security.token;


import feign.FeignException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
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

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationProcessingFilter.class);
    @Autowired
    private UserProxy userProxy;

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration.token.auth}")
    private Long authTokenExpiration;
    @Value("${jwt.expiration.token.refresh}")
    private Long refreshTokenExpiration;
    @Value("${jwt.prefix}")
    private String tokenPrefix;
    @Value("${jwt.prefix.authorities}")
    private String authoritiesPrefix;
    @Value("${jwt.prefix.active.refresh}")
    private String activePrefix;

    @Override
    public String createAuthToken(User userContext) throws IllegalArgumentException {
        if (userContext.getUserName() == null || userContext.getUserName().isEmpty())
            throw new IllegalArgumentException("jwt.auth.username.null");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("jwt.auth.authorities.null");

        logger.debug("Create authToken for userName " + userContext.getUserName());
        // construction du Json web token
        return Jwts.builder().setSubject(userContext.getUserName()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + authTokenExpiration)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, secret) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(authoritiesPrefix, userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList())) // ajout personnalisé --> on ajoute les roles
                .compact(); // construction du token
    }

    @Override
    public String createRefreshToken(User userContext) throws IllegalArgumentException{

        if (userContext.getUserName() == null || userContext.getUserName().isEmpty())
            throw new IllegalArgumentException("jwt.refresh.username.null");


        logger.debug("Create refreshToken for userName " + userContext.getUserName());
        // construction du Json web token
        return Jwts.builder().setSubject(userContext.getUserName()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, secret) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(activePrefix, true) // ajout de vérification pour le rafraichissement
                .compact(); // construction du token

    }

    @Override
    public User validateRefreshToken(JwtToken token) throws AuthenticationException, FeignException {

        // vérification du token
        Jws<Claims> claims = token.parseClaims(token.getToken(), secret);

        // on recupere le contact pour comparaison
        User contact = userProxy.findByUserName(claims.getBody().getSubject());

        // si le contact est toujours bon, alors le token est toujours valide
        if (contact.isActive() != (boolean) claims.getBody().get(activePrefix))
            throw new DisabledException("contact.not.active");

        contact.setAuthorities(Role.getListAuthorities(contact.getRoles()));
        logger.debug("Validate refreshToken for userName " + contact.getUserName());
        return contact;
    }

    @Override
    public String extract(String header) throws BadCredentialsException {

        if (header == null || header.isEmpty()) {
            throw new BadCredentialsException("authorization.header.blank");
        }

        return header.substring(tokenPrefix.length(), header.length());
    }

}
