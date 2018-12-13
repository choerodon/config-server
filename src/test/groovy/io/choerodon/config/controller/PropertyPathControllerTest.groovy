package io.choerodon.config.controller

import io.choerodon.config.config.ConfigServerProperties
import org.springframework.cloud.client.ServiceInstance
import org.springframework.cloud.client.discovery.DiscoveryClient
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class PropertyPathControllerTest extends Specification {


    def "test notify By Path"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def properties = new ConfigServerProperties()
        properties.setNotifyEndpoint("")

        and: 'mock DiscoveryClient'
        def serviceInstance = Mock(ServiceInstance) {
            getMetadata() >> new HashMap<String, String>()
        }
        def discoveryClient = Mock(DiscoveryClient) {
            getInstances(_) >> [serviceInstance]
        }

        def controller = new PropertyPathController(discoveryClient, properties)
        controller.setRestTemplate(restTemplate)

        and:
        def map = new LinkedHashMap()
        map.put("path", "api-gateway")

        when:
        def set = controller.notifyByPath(map)

        then:
        1 * restTemplate.put(_, _)
        set.size() == 1
    }
}