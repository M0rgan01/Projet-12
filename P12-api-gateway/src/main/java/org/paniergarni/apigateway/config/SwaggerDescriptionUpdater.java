package org.paniergarni.apigateway.config;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


/**
 *
 * @author Pichat morgan
 * <pre>
 *   Vérifie périodiquement le service d'instance pour mettre à jour la documention swagger en mémoire
 * </pre>
 */
@Component
public class SwaggerDescriptionUpdater {
    private static final Logger logger = LoggerFactory.getLogger(SwaggerDescriptionUpdater.class);

    private static final String DEFAULT_SWAGGER_URL="/v2/api-docs";
    private static final String KEY_SWAGGER_URL="swagger_url";

    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate template;

    public SwaggerDescriptionUpdater(){
        this.template = new RestTemplate();
    }

    @Autowired
    private SwaggerDefinitionContext definitionContext;


    /**
     * Vérification du service d'instance périodiquement
     */
    @Scheduled(fixedDelayString= "${swagger.config.refresh}")
    public void refreshSwaggerConfigurations(){
        logger.debug("Starting Service Definition Context refresh");

        discoveryClient.getServices().stream().forEach(serviceId -> {
            logger.debug("Attempting service definition refresh for Service : {} ", serviceId);
            List<ServiceInstance> serviceInstances = discoveryClient.getInstances(serviceId);
            if(serviceInstances == null || serviceInstances.isEmpty()){
                logger.info("No instances available for service : {} ",serviceId);
            }else{
                ServiceInstance instance = serviceInstances.get(0);
                String swaggerURL =  getSwaggerURL( instance);

                Optional<Object> jsonData = getSwaggerDefinitionForAPI(serviceId, swaggerURL);

                if(jsonData.isPresent()){
                    String content = getJSON(serviceId, jsonData.get());
                    definitionContext.addSwaggerDefinition(serviceId, content);
                }else{
                    logger.error("Skipping service id : {} Error : Could not get Swagger definition from API ",serviceId);
                }

                logger.debug("Service Definition Context Refreshed at :  {}", LocalDate.now());
            }
        });
    }

    /**
     * Vérification de l'url, dans le cas ou une modification à été effectué
     */
    private String getSwaggerURL( ServiceInstance instance){
        String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
        return swaggerURL != null ? instance.getUri()+swaggerURL : instance.getUri()+DEFAULT_SWAGGER_URL;
    }

    /**
     * @return définition du service par rapport à son URL
     */
    private Optional<Object> getSwaggerDefinitionForAPI(String serviceName, String url){
        logger.debug("Accessing the SwaggerDefinition JSON for Service : {} : URL : {} ", serviceName, url);
        try{
            Object jsonData = template.getForObject(url, Object.class);
            return Optional.of(jsonData);
        }catch(RestClientException ex){
            logger.error("Error while getting service definition for service : {} Error : {} ", serviceName, ex.getMessage());
            return Optional.empty();
        }

    }

    public String getJSON(String serviceId, Object jsonData){
        try {
            return new ObjectMapper().writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            logger.error("Error : {} ", e.getMessage());
            return "";
        }
    }
}
