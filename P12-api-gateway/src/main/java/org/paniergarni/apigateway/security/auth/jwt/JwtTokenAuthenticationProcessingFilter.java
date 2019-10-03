package org.paniergarni.apigateway.security.auth.jwt;


import org.paniergarni.apigateway.security.auth.login.LoginAuthenticationProvider;
import org.paniergarni.apigateway.security.token.JwtAuthenticationToken;
import org.paniergarni.apigateway.security.token.JwtService;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre de mise en place d'authentification par JWT 
 * 
 * 
 * @author Pichat morgan
 *
 * 20 Juillet 2019
 */
public class JwtTokenAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationProcessingFilter.class);
    private final AuthenticationFailureHandler failureHandler;
    private final JwtService jwtService;
    private String headerAuth;

    @Autowired
    public JwtTokenAuthenticationProcessingFilter(AuthenticationFailureHandler failureHandler, 
    		JwtService jwtService, RequestMatcher matcher, String headerAuth) {
        super(matcher);
        this.failureHandler = failureHandler;
        this.jwtService = jwtService;
        this.headerAuth = headerAuth;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

    	//on récupère le token
        String tokenPayload = request.getHeader(headerAuth);
         logger.debug("Getting token from payload for authentication");
        // on enlève le préfixe et on créé un objet JwtToken
        JwtToken token = new JwtToken(jwtService.extract(tokenPayload));
              
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(token));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        failureHandler.onAuthenticationFailure(request, response, failed);
    }
}
