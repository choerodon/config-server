package io.choerodon.config.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author flyleft
 */
@ConfigurationProperties(prefix = "choerodon.config.server")
public class ChoerodonConfigServerProperties {


    private String managerService = "manager-service";


    public String getManagerService() {
        return managerService;
    }

    public void setManagerService(String managerService) {
        this.managerService = managerService;
    }

}
