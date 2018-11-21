package io.choerodon.config.controller

import io.choerodon.config.config.ConfigServerProperties
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

class PropertyPathControllerTest extends Specification {


    def "test notify By Path"() {
        given:
        def restTemplate = Mock(RestTemplate)
        def properties = new ConfigServerProperties()
        properties.setNotifyEndpoint("")
        def controller = new PropertyPathController(restTemplate, properties)

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