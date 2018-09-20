package io.choerodon.config.service

import io.choerodon.config.IntegrationTestConfiguration
import io.choerodon.config.domain.Config
import io.choerodon.config.feign.ManagerServiceClient
import io.choerodon.core.exception.CommonException
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
class PullConfigServiceSpec extends Specification {

    def '测试 getConfig方法'() {
        given: 'mock feign'
        def config = new Config()
        config.setValue(new HashMap<String, Object>())
        config.setName("api-gateway")
        def feign = Mock(ManagerServiceClient) {
            getConfig(_, _) >> config
        }

        and: '注入PullConfigService'
        def pullConfigService = new PullConfigServiceImpl(feign)

        when: '当configVersion为default时'
        pullConfigService.getConfig("test-service", "default")

        then: '抛出CommonException'
        1 * feign.getConfig(_)
        thrown CommonException

        when: '当configVersion不为时'
        def getConfig =  pullConfigService.getConfig("test-service", "v1")

        then: 'feign的getConfig(serviceName, configVersion)被调用'
        getConfig == config
    }

}
