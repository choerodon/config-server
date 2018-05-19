package io.choerodon.config.service;

import io.choerodon.config.domain.Config;
import io.choerodon.config.feign.ManagerServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

/**
 * @author flyleft
 */
@Service
public class PullConfigServiceImpl implements PullConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PullConfigService.class);

    private ManagerServiceClient managerServiceClient;

    public PullConfigServiceImpl(ManagerServiceClient managerServiceClient) {
        this.managerServiceClient = managerServiceClient;
    }

    @Override
    public Config getConfig(String serviceName, String configVersion) {
        Config config;
        try {
            if (StringUtils.isEmpty(configVersion)) {
                config = managerServiceClient.getConfig(serviceName);
            } else {
                config = managerServiceClient.getConfig(serviceName, configVersion);
            }
            LOGGER.info("serviceName {} configVersion{} config {}", serviceName, configVersion, config);
        } catch (Exception e) {
            LOGGER.warn("error.pullConfigService.getConfig, {} {}", serviceName, configVersion);
            config = new Config();
            config.setConfigVersion(configVersion);
            config.setValue(new HashMap<>(0));
        }
        return config;
    }


}
