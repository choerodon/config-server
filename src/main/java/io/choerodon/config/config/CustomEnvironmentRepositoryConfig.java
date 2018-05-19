package io.choerodon.config.config;

import io.choerodon.config.DbEnvironmentRepository;
import io.choerodon.config.service.PullConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.config.server.config.ConfigServerHealthIndicator;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 用于使DbEnvironmentRepository生效
 *
 * @author crock
 */
@Configuration
public class CustomEnvironmentRepositoryConfig {

    @Autowired
    private PullConfigService pullConfigService;

    @Value("${spring.application.name:config-server}")
    private String application;

    /**
     * 将EnvironmentRepository载入spring工厂
     *
     * @return 自定义的EnvironmentRepository
     */
    @Bean
    @ConditionalOnMissingBean(EnvironmentRepository.class)
    public SearchPathCompositeEnvironmentRepository searchPathCompositeEnvironmentRepository() {
        DbEnvironmentRepository dbEnvironmentRepository = new DbEnvironmentRepository(new ArrayList<>());
        dbEnvironmentRepository.setPullConfigService(pullConfigService);
        return dbEnvironmentRepository;
    }

    /**
     * spring内部的健康检查
     *
     * @param repository 配置环境拉取策略
     * @return 健康检查实现类
     */
    @Bean
    @ConditionalOnProperty(value = "spring.cloud.config.server.health.enabled", matchIfMissing = true)
    public ConfigServerHealthIndicator configServerHealthIndicator(EnvironmentRepository repository) {
        ConfigServerHealthIndicator configServerHealthIndicator = new ConfigServerHealthIndicator(repository);
        Map<String, ConfigServerHealthIndicator.Repository> map = new LinkedHashMap<>();
        map.put(application, new ConfigServerHealthIndicator.Repository());
        configServerHealthIndicator.setRepositories(map);
        return configServerHealthIndicator;
    }

    @Configuration
    @Profile("git")
    @ConditionalOnMissingBean(EnvironmentRepository.class)
    protected static class DefaultRepositoryConfiguration {

        @Autowired
        private ConfigurableEnvironment environment;

        @Autowired
        private ConfigServerProperties configServerProperties;

        protected DefaultRepositoryConfiguration() {
        }

        @Bean
        public MultipleJGitEnvironmentRepository defaultEnvironmentRepository() {
            MultipleJGitEnvironmentRepository repository = new MultipleJGitEnvironmentRepository(this.environment);
            if (this.configServerProperties.getDefaultLabel() != null) {
                repository.setDefaultLabel(this.configServerProperties.getDefaultLabel());
            }

            return repository;
        }
    }

    @Configuration
    @Profile("native")
    protected static class NativeRepositoryConfiguration {

        @Autowired
        private ConfigurableEnvironment environment;

        @Bean
        public NativeEnvironmentRepository nativeEnvironmentRepository() {
            return new NativeEnvironmentRepository(this.environment);
        }
    }

    @Configuration
    @Profile("git")
    protected static class GitRepositoryConfiguration extends DefaultRepositoryConfiguration {
    }

    @Configuration
    @Profile("subversion")
    protected static class SvnRepositoryConfiguration {

        @Autowired
        private ConfigurableEnvironment environment;

        @Autowired
        private ConfigServerProperties configServerProperties;

        @Bean
        public SvnKitEnvironmentRepository svnKitEnvironmentRepository() {
            SvnKitEnvironmentRepository repository = new SvnKitEnvironmentRepository(this.environment);
            if (this.configServerProperties.getDefaultLabel() != null) {
                repository.setDefaultLabel(this.configServerProperties.getDefaultLabel());
            }
            return repository;
        }
    }

    @Configuration
    @Profile("vault")
    protected static class VaultConfiguration {
        @Bean
        public VaultEnvironmentRepository vaultEnvironmentRepository(HttpServletRequest request, EnvironmentWatch watch) {
            return new VaultEnvironmentRepository(request, watch, new RestTemplate());
        }
    }

    @Configuration
    @ConditionalOnProperty(value = "spring.cloud.config.server.consul.watch.enabled")
    protected static class ConsulEnvironmentWatchConfiguration {
        @Bean
        public EnvironmentWatch environmentWatch() {
            return new ConsulEnvironmentWatch();
        }
    }

    @Configuration
    @ConditionalOnMissingBean(EnvironmentWatch.class)
    protected static class DefaultEnvironmentWatch {
        @Bean
        public EnvironmentWatch environmentWatch() {
            return new EnvironmentWatch.Default();
        }
    }
}
