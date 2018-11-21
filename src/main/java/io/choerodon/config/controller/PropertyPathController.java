package io.choerodon.config.controller;

import io.choerodon.config.config.ConfigServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(path = "${spring.cloud.config.monitor.endpoint.path:}/monitor")
public class PropertyPathController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyPathController.class);

    private RestTemplate restTemplate;

    private ConfigServerProperties properties;

    public PropertyPathController(@Qualifier("ribbonRestTemplate") RestTemplate restTemplate,
                                  ConfigServerProperties properties) {
        this.restTemplate = restTemplate;
        this.properties = properties;
    }

    @PostMapping
    @SuppressWarnings("unchecked")
    public Set<String> notifyByPath(@RequestBody Map<String, Object> request) {
        Set<String> services = new LinkedHashSet<>();
        Object object = request.get("path");
        if (object instanceof String) {
            services.add((String) object);
        }
        if (object instanceof Collection) {
            Collection<String> collection = (Collection<String>) object;
            services.addAll(collection);
        }
        services.forEach(service -> {
            try {
                String noticeUri = "http://" + service + properties.getNotifyEndpoint();
                restTemplate.put(noticeUri, null);
            } catch (Exception e) {
                LOGGER.warn("Notify service: {} refresh config failed", service, e);
            }
        });
        return services;
    }

}
