package org.paniergarni.apigateway.controller;

import org.paniergarni.apigateway.config.SwaggerDefinitionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Pichat morgan
 * <pre>
 *   Controller de récupération de document swagger d'un service
 * </pre>
 */
@RestController
public class SwaggerDefinitionController {


    @Autowired
    private SwaggerDefinitionContext definitionContext;

    @GetMapping("/swagger/{serviceName}")
    public String getServiceDefinition(@PathVariable("serviceName") String serviceName){

        return definitionContext.getSwaggerDefinition(serviceName);

    }
}
