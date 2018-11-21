package io.choerodon.config.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.config.server.config.ConfigServerEncryptionConfiguration;
import org.springframework.cloud.config.server.config.ConfigServerMvcConfiguration;
import org.springframework.cloud.config.server.config.ResourceRepositoryConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author flyleft
 */
@Configuration
@EnableConfigurationProperties({ConfigServerProperties.class, org.springframework.cloud.config.server.config.ConfigServerProperties.class})
@Import({CustomEnvironmentRepositoryConfig.class, ResourceRepositoryConfiguration.class, ConfigServerEncryptionConfiguration.class, ConfigServerMvcConfiguration.class})
public class ConfigServerConfig {
}