package io.choerodon.config.controller;

import io.choerodon.config.config.ConfigServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping(path = "${spring.cloud.config.monitor.endpoint.path:}/monitor")
public class PropertyPathController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertyPathController.class);

    private static final String METADATA_CONTEXT_PATH = "CONTEXT-PATH";

    private final RestTemplate restTemplate = new RestTemplate();

    private ConfigServerProperties properties;

    private DiscoveryClient discoveryClient;

    public PropertyPathController(DiscoveryClient discoveryClient,
                                  ConfigServerProperties properties) {
        this.properties = properties;
        this.discoveryClient = discoveryClient;
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
                List<ServiceInstance> instances = discoveryClient.getInstances(service);
                if (instances.isEmpty()) {
                    LOGGER.warn("Notify service: {} refresh config failed, this service is not UP", service);
                } else {
                    instances.forEach(i -> {
                        String contextPath = i.getMetadata().get(METADATA_CONTEXT_PATH);
                        String noticeUri = "http://" + i.getHost() + ":" + i.getPort();
                        if (!StringUtils.isEmpty(contextPath)) {
                            noticeUri = noticeUri + "/" + contextPath;
                        }
                        noticeUri = noticeUri + properties.getNotifyEndpoint();
                        restTemplate.put(noticeUri, null);
                    });
                }

            } catch (Exception e) {
                LOGGER.warn("Notify service: {} refresh config failed", service, e);
            }
        });
        return services;
    }

}
