package org.paniergarni.apigateway.controller;

import feign.FeignException;
import io.swagger.annotations.*;
import org.paniergarni.apigateway.object.CreateUserDTO;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.token.JwtService;
import org.paniergarni.apigateway.security.token.JwtToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api( description="API d'inscription, de connection d'utilisateur et de rafraichissement du token")
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

    @ApiOperation(value = "Connection d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de la connction"),
            @ApiResponse(code = 409, message = "UserName / Email / PassWord incorrect"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "MC account indisponnible")
    })
    @PostMapping("/api/auth/login")
    public void fakeLogin(@ApiParam("User") @RequestParam String userName, @ApiParam("Password") @RequestParam String passWord) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation(value = "Inscription d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès de l' inscription"),
            @ApiResponse(code = 409, message = "UserName / Email / PassWord incorrect"),
            @ApiResponse(code = 412, message = "Erreur du JSON"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "MC account indisponnible")
    })
    @PostMapping(value = "/api/auth/register")
    public ResponseEntity<?> register(@RequestBody CreateUserDTO createUserDTO) throws IllegalArgumentException, FeignException {

        User user = userProxy.createUser(createUserDTO);
        user.setAuthorities(Role.getListAuthorities(user.getRoles()));
        // on créer un token JWT
        String jwt = jwtService.createAuthToken(user);
        String jwtRefresh = jwtService.createRefreshToken(user);

        // on l'ajoute au headers de la réponse
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(headerAuth, tokenPrefix + jwt);
        responseHeaders.add(headerRefresh, tokenPrefix + jwtRefresh);

        return ResponseEntity.ok().headers(responseHeaders).body(user);
    }

    @ApiOperation(value = "Rafraichissement d'un token d'authentification")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succès du rafraichissement"),
            @ApiResponse(code = 409, message = "Token incorrect, inexistant"),
            @ApiResponse(code = 500, message = "Erreur interne"),
            @ApiResponse(code = 503, message = "MC account indisponnible")
    })
    @GetMapping(value = "/api/auth/token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws IllegalArgumentException, AuthenticationException, FeignException {

        JwtToken token = new JwtToken(jwtService.extract(request.getHeader(headerRefresh)));

        // on créer un token JWT, grace à la vérification du tokenRefresh
        String jwt = jwtService.createAuthToken(jwtService.validateRefreshToken(token));

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add(headerAuth, tokenPrefix + jwt);
        responseHeaders.add(headerRefresh, request.getHeader(headerRefresh));
        return ResponseEntity.ok().headers(responseHeaders).body(null);

    }

}
