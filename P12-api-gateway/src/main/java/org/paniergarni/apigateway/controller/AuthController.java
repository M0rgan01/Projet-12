package org.paniergarni.apigateway.controller;

import feign.FeignException;
import io.jsonwebtoken.ExpiredJwtException;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.SecurityConstants;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.response.ErrorResponse;
import org.paniergarni.apigateway.security.token.JwtService;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {


    @Autowired
    private UserProxy userProxy;
    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody User contact) {

        try {

           contact = userProxy.createUser(contact);

            // on créer un token JWT
            String jwt = jwtService.createAuthToken(UserContext.create(contact.getUserName(), Role.getListAuthorities(contact.getRoles())));
            String jwtRefresh = jwtService.createRefreshToken(UserContext.create(contact.getUserName(), null));

            // on l'ajoute au headers de la réponse
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(SecurityConstants.HEADER_AUTH_STRING, SecurityConstants.TOKEN_PREFIX + jwt);
            responseHeaders.add(SecurityConstants.HEADER_REFRESH_STRING, SecurityConstants.TOKEN_PREFIX + jwtRefresh);

            return ResponseEntity.ok().headers(responseHeaders).body(contact);

        } catch (FeignException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorResponse.of(e.contentUTF8(), e.getClass().getCanonicalName(), HttpStatus.NOT_ACCEPTABLE));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorResponse.of(e.getMessage(), e.getClass().getCanonicalName(), HttpStatus.NOT_ACCEPTABLE));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(e.getMessage(), e.getClass().getCanonicalName(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping(value = "/api/auth/token")
    public ResponseEntity<?> refreshToken(@RequestHeader(SecurityConstants.HEADER_REFRESH_STRING) String tokenRefresh) {

        try {
            JwtToken token = new JwtToken(jwtService.extract(tokenRefresh));

            // on créer un token JWT, grace à la vérification du tokenRefresh
            String jwt = jwtService.createAuthToken(jwtService.validateRefreshToken(token));

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(SecurityConstants.HEADER_AUTH_STRING, SecurityConstants.TOKEN_PREFIX + jwt);
            responseHeaders.add(SecurityConstants.HEADER_REFRESH_STRING, tokenRefresh);
            return ResponseEntity.ok().headers(responseHeaders).body(null);

        } catch (Exception e) {
            if (e instanceof BadCredentialsException || e instanceof  ExpiredJwtException)
                return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(ErrorResponse.of(e.getMessage(), e.getClass().getCanonicalName(), HttpStatus.NOT_ACCEPTABLE));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.of(e.getMessage(), e.getClass().getCanonicalName(), HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
