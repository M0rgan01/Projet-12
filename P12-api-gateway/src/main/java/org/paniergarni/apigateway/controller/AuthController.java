package org.paniergarni.apigateway.controller;

import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.token.JwtService;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class AuthController {

    @Value("${jwt.prefix}")
    private String tokenPrefix;
    @Value("${jwt.header.token.auth}")
    private String headerAuth;
    @Value("${jwt.header.token.refresh}")
    private String headerRefresh;
    @Autowired
    private UserProxy userProxy;
    @Autowired
    private JwtService jwtService;

    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        user = userProxy.createUser(user);
        // on créer un token JWT
        String jwt = jwtService.createAuthToken(UserContext.create(user.getUserName(), Role.getListAuthorities(user.getRoles())));
        String jwtRefresh = jwtService.createRefreshToken(UserContext.create(user.getUserName(), null));

        // on l'ajoute au headers de la réponse
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(headerAuth, tokenPrefix + jwt);
        responseHeaders.add(headerRefresh, tokenPrefix + jwtRefresh);

        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }

    @GetMapping(value = "/api/auth/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {

        JwtToken token = new JwtToken(jwtService.extract(request.getHeader(headerRefresh)));

        // on créer un token JWT, grace à la vérification du tokenRefresh
        String jwt = jwtService.createAuthToken(jwtService.validateRefreshToken(token));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(headerAuth, tokenPrefix + jwt);
        responseHeaders.add(headerRefresh, request.getHeader(headerRefresh));
        return ResponseEntity.ok().headers(responseHeaders).body(null);

    }

}
