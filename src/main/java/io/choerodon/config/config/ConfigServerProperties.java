package io.choerodon.config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author flyleft
 */
@ConfigurationProperties(prefix = "choerodon.config.server")
public class ConfigServerProperties {

    private String managerService = "manager-service";

    private String notifyEndpoint = "/choerodon/config";

    public String getManagerService() {
        return managerService;
    }

    public void setManagerService(String managerService) {
        this.managerService = managerService;
    }

    public String getNotifyEndpoint() {
        return notifyEndpoint;
    }

    public void setNotifyEndpoint(String notifyEndpoint) {
        this.notifyEndpoint = notifyEndpoint;
    }
}
