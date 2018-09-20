package io.choerodon.config.service

import io.choerodon.config.IntegrationTestConfiguration
import io.choerodon.config.config.ChoerodonConfigServerProperties
import io.choerodon.config.domain.Config
import io.choerodon.core.exception.CommonException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(IntegrationTestConfiguration)
class DbEnvironmentRepositorySpec extends Specification {

    @Autowired
    DbEnvironmentRepository environmentRepository

    @Autowired
    private ChoerodonConfigServerProperties configServerProperties

    @Value("\${spring.application.name:config-server}")
    private String applicationName

    def '测试 getLocations'() {
        given: '创建测试参数'
        def application = 'api-gateway'
        def profile = 'aaa'
        def label = 'label'

        when: '调用 getLocations方法'
        def location = environmentRepository.getLocations(application, profile, label)

        then: '验证结果'
        location.getApplication() == application
        location.getLabel() == label
        location.getProfile() == profile
        location.getVersion() == null
        location.getLocations()[0] == configServerProperties.getManagerService()
    }

    def '测试 findOne'() {
        given: '创建测试参数'
        def application = 'api-gateway'
        def profile = 'aaa'
        def label = 'label'
        def config = new Config()
        config.setConfigVersion("v1")

        and: 'mock PullConfigService'
        def exceptionPullConfigService = Mock(PullConfigService) {
            getConfig(_,_) >> { throw  new CommonException('')}
        }
        def pullConfigService = Mock(PullConfigService) {
            getConfig(_,_) >> config
        }
        environmentRepository.setPullConfigService(pullConfigService)

        when: '当application为本服务时调用findOne方法'
        def env =environmentRepository.findOne(applicationName, profile, label)

        then: '验证env'
        env.getName() == configServerProperties.getManagerService()

        when: '当PullConfigService正常时调用findOne方法'
        def env1 =environmentRepository.findOne(application, profile, label)

        then: '验证env'
        env1.getVersion() == config.getConfigVersion()
        env1.getName() == configServerProperties.getManagerService()

        when: '当PullConfigService有异常时调用findOne方法'
        environmentRepository.setPullConfigService(exceptionPullConfigService)
        def env2 =environmentRepository.findOne(application, profile, label)

        then: '验证env'
        env2.getVersion() == config.getConfigVersion()
        env2.getName() == configServerProperties.getManagerService()

        when: '当PullConfigService有异常且无缓存时调用findOne方法'
        environmentRepository.findOne(application, profile, 'new label')

        then: '抛出异常'
        thrown CommonException
    }
}
