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
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.exception.ProxyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
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
    public String createAuthToken(UserContext userContext) {
        if (userContext.getUsername() == null || userContext.getUsername().isEmpty())
            throw new IllegalArgumentException("jwt.auth.username.null");

        if (userContext.getAuthorities() == null || userContext.getAuthorities().isEmpty())
            throw new IllegalArgumentException("jwt.auth.authorities.null");

        // construction du Json web token
        return Jwts.builder().setSubject(userContext.getUsername()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + authTokenExpiration)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, secret) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(authoritiesPrefix, userContext.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toList())) // ajout personnalisé --> on ajoute les roles
                .compact(); // construction du token
    }

    @Override
    public String createRefreshToken(UserContext userContext) {

        if (userContext.getUsername() == null || userContext.getUsername().isEmpty())
            throw new IllegalArgumentException("jwt.refresh.username.null");

        // construction du Json web token
        return Jwts.builder().setSubject(userContext.getUsername()) // ajout de username
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration)) // ajout d'une date d'expiration
                .signWith(SignatureAlgorithm.HS256, secret) // partie secrete servant de clé, avec un algorithme de type HS 256
                .claim(activePrefix, true) // ajout de vérification pour le rafraichissement
                .compact(); // construction du token

    }

    @Override
    public UserContext validateRefreshToken(JwtToken token) {

        // vérification du token
        Jws<Claims> claims = token.parseClaims(token.getToken(), secret);

        // on recupere le contact pour comparaison
        User contact;
        try {
            contact = userProxy.findByUserName(claims.getBody().getSubject());
        } catch (FeignException e1) {
            if (e1 instanceof RetryableException)
                throw new AuthenticationServiceException("internal.error");
            throw new UsernameNotFoundException("user.not.found");
            // entre le temps ou le proxy est indisponible et est toujours sur eureka
        } catch (Exception e) {
            throw new ProxyException("internal.error");
        }

        // si le contact est toujours bon, alors le token est toujours valide
        if (contact.isActive() != (boolean) claims.getBody().get(activePrefix))
            throw new DisabledException("contact.not.active");

        return UserContext.create(claims.getBody().getSubject(), Role.getListAuthorities(contact.getRoles()));
    }

    @Override
    public String extract(String header) {

        if (header == null || header.isEmpty()) {
            throw new BadCredentialsException("authorization.header.blank");
        }

        return header.substring(tokenPrefix.length(), header.length());
    }

}
