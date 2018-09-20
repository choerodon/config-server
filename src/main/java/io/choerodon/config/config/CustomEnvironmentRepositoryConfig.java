package io.choerodon.config.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.Decoder;
import io.choerodon.config.service.DbEnvironmentRepository;
import io.choerodon.config.service.PullConfigService;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.config.server.config.ConfigServerHealthIndicator;
import org.springframework.cloud.config.server.config.ConfigServerProperties;
import org.springframework.cloud.config.server.environment.*;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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

    @Value("${spring.application.name:config-server}")
    private String application;

    /**
     * 将EnvironmentRepository载入spring工厂
     *
     * @return 自定义的EnvironmentRepository
     */
    @Bean
    @ConditionalOnMissingBean(EnvironmentRepository.class)
    public SearchPathCompositeEnvironmentRepository searchPathCompositeEnvironmentRepository(PullConfigService pullConfigService) {
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
        @Bean
        public MultipleJGitEnvironmentRepository defaultEnvironmentRepository(ConfigurableEnvironment environment,
                                                                              ConfigServerProperties configServerProperties) {
            MultipleJGitEnvironmentRepository repository = new MultipleJGitEnvironmentRepository(environment);
            if (configServerProperties.getDefaultLabel() != null) {
                repository.setDefaultLabel(configServerProperties.getDefaultLabel());
            }

            return repository;
        }
    }

    @Configuration
    @Profile("native")
    protected static class NativeRepositoryConfiguration {

        @Bean
        public NativeEnvironmentRepository nativeEnvironmentRepository(ConfigurableEnvironment environment) {
            return new NativeEnvironmentRepository(environment);
        }
    }

    @Configuration
    @Profile("git")
    protected static class GitRepositoryConfiguration extends DefaultRepositoryConfiguration {
    }

    @Configuration
    @Profile("subversion")
    protected static class SvnRepositoryConfiguration {

        @Bean
        public SvnKitEnvironmentRepository svnKitEnvironmentRepository(ConfigurableEnvironment environment,
                                                                       ConfigServerProperties configServerProperties) {
            SvnKitEnvironmentRepository repository = new SvnKitEnvironmentRepository(environment);
            if (configServerProperties.getDefaultLabel() != null) {
                repository.setDefaultLabel(configServerProperties.getDefaultLabel());
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

    /**
     * 配置feign解码忽略不匹配的字段
     */
    @Bean
    public Decoder feignDecoder() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        HttpMessageConverter jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }
}

