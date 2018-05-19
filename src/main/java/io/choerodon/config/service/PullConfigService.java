package io.choerodon.config.service;

import io.choerodon.config.domain.Config;

/**
 * @author flyleft
 */

public interface PullConfigService {

    Config getConfig(String serviceName, String configVersion);

}
