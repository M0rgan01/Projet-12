package org.paniergarni.apigateway.security.auth.login;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.RetryableException;
import org.paniergarni.apigateway.object.Role;
import org.paniergarni.apigateway.object.User;
import org.paniergarni.apigateway.proxy.UserProxy;
import org.paniergarni.apigateway.security.auth.model.UserContext;
import org.paniergarni.apigateway.security.exception.ProxyException;
import org.paniergarni.apigateway.security.response.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * Processus d'authentification par login
 *
 * @author Pichat morgan
 * <p>
 * 20 Juillet 2019
 */
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private UserProxy userProxy;
    private ObjectMapper objectMapper;

    @Autowired
    public LoginAuthenticationProvider(UserProxy userProxy, ObjectMapper objectMapper) {
        this.userProxy = userProxy;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // vérification de l'objet authentication
        Assert.notNull(authentication, "No authentication data provided");

        // récupération des information de connection
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // récupération du contact pour la comparaison
        User contact = null;

        try {
            contact = userProxy.userConnection(username, password);
        } catch (FeignException e) {
            // si l'instance est encore dans le register mais down
            if (e instanceof RetryableException)
                throw new AuthenticationServiceException("internal.error");

            //on récupère le message d'erreur d'origine
            ErrorResponse errorResponse;

            try {
                errorResponse = objectMapper.readValue(e.content(), ErrorResponse.class);
            } catch (Exception e1) {
                throw new AuthenticationServiceException("internal.error");
            }

            throw new ProxyException(errorResponse.getError());

            // si l'instance n'est plus dans le register
        } catch (Exception e) {
            throw new AuthenticationServiceException("internal.error");
        }

        // vérification des roles
        if (contact.getRoles() == null)
            throw new InsufficientAuthenticationException("user.roles.null");

        UserContext userContext = UserContext.create(contact.getUserName(),
                Role.getListAuthorities(contact.getRoles()));

        return new UsernamePasswordAuthenticationToken(userContext, null, userContext.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
