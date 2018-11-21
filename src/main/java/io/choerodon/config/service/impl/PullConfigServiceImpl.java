package io.choerodon.config.service.impl;

import io.choerodon.config.domain.Config;
import io.choerodon.config.feign.ManagerServiceClient;
import io.choerodon.config.service.PullConfigService;
import io.choerodon.core.exception.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author flyleft
 */
@Service
public class PullConfigServiceImpl implements PullConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullConfigService.class);

    private static final String DEFAULT_CONFIG_VERSION = "default";

    private ManagerServiceClient managerServiceClient;

    public PullConfigServiceImpl(ManagerServiceClient managerServiceClient) {
        this.managerServiceClient = managerServiceClient;
    }

    @Override
    public Config getConfig(String serviceName, String configVersion) {
        Config config;
        if (StringUtils.isEmpty(configVersion) || DEFAULT_CONFIG_VERSION.equals(configVersion)) {
            config = managerServiceClient.getConfig(serviceName);
        } else {
            config = managerServiceClient.getConfig(serviceName, configVersion);
        }
        if (config == null || config.getName() == null) {
            LOGGER.info("error.pullConfig serviceName {} configVersion {}", serviceName, configVersion);
            throw new CommonException("error.pullConfig serviceName " + serviceName + " configVersion " + configVersion);
        } else {
            LOGGER.info("pullConfig success, serviceName {} configVersion{} config {}", serviceName, configVersion, config);
        }
        return config;
    }

}
