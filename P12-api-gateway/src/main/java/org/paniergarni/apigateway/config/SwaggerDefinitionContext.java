package org.paniergarni.apigateway.config;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 *
 * @author PICHAT morgan
 * <pre>
 *      Stock mémoire des définition API swagger en JSON
 * </pre>
 */
@Component
@Scope(scopeName= ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SwaggerDefinitionContext {

    private final ConcurrentHashMap<String,String> serviceDescriptions;

    private SwaggerDefinitionContext(){
        serviceDescriptions = new ConcurrentHashMap<String, String>();
    }

    public void addSwaggerDefinition(String serviceName, String serviceDescription){
        serviceDescriptions.put(serviceName, serviceDescription);
    }

    public String getSwaggerDefinition(String serviceId){
        return this.serviceDescriptions.get(serviceId);
    }

    public List<SwaggerResource> getSwaggerDefinitions(){
        return  serviceDescriptions.entrySet().stream().map( serviceDefinition -> {
            SwaggerResource resource = new SwaggerResource();
            resource.setLocation("/api/"+serviceDefinition.getKey() + "/v2/api-docs");
            resource.setName(serviceDefinition.getKey());
            resource.setSwaggerVersion("2.0");
            return resource;
        }).collect(Collectors.toList());
    }
}
