package org.paniergarni.apigateway.controller;

import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.SecurityConstants;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.token.JwtService;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AuthController {


    @Autowired
    private UserProxy userProxy;
    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody User contact) {

            contact = userProxy.createUser(contact);
            // on créer un token JWT
            String jwt = jwtService.createAuthToken(UserContext.create(contact.getUserName(), Role.getListAuthorities(contact.getRoles())));
            String jwtRefresh = jwtService.createRefreshToken(UserContext.create(contact.getUserName(), null));

            // on l'ajoute au headers de la réponse
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(SecurityConstants.HEADER_AUTH_STRING, SecurityConstants.TOKEN_PREFIX + jwt);
            responseHeaders.add(SecurityConstants.HEADER_REFRESH_STRING, SecurityConstants.TOKEN_PREFIX + jwtRefresh);

            return ResponseEntity.ok().headers(responseHeaders).body(contact);
    }

    @GetMapping(value = "/api/auth/token")
    public ResponseEntity<?> refreshToken(@RequestHeader(SecurityConstants.HEADER_REFRESH_STRING) String tokenRefresh) {


            JwtToken token = new JwtToken(jwtService.extract(tokenRefresh));

            // on créer un token JWT, grace à la vérification du tokenRefresh
            String jwt = jwtService.createAuthToken(jwtService.validateRefreshToken(token));

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add(SecurityConstants.HEADER_AUTH_STRING, SecurityConstants.TOKEN_PREFIX + jwt);
            responseHeaders.add(SecurityConstants.HEADER_REFRESH_STRING, tokenRefresh);
            return ResponseEntity.ok().headers(responseHeaders).body(null);

    }

}
