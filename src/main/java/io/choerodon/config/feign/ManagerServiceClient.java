package io.choerodon.config.feign;

import io.choerodon.config.domain.Config;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 调用manager管理服务的feign
 * @author crock
 */
@FeignClient(name = "${choerodon.manager.service:manager-service}")
public interface ManagerServiceClient {

    /**
     * 向配置管理服务索要配置
     * @param serviceName 应用名
     * @return 配置集
     */
    @GetMapping("/v1/services/{service_name}/configs/default")
    Config getConfig(@PathVariable("service_name") String serviceName);

    /**
     * 向配置管理服务索要配置
     * @param serviceName 应用名
     * @param configVersion 配置版本
     * @return 配置集
     */
    @GetMapping("/v1/services/{service_name}/configs/{config_version}")
    Config getConfig(@PathVariable("service_name") String serviceName,
                     @PathVariable("config_version") String configVersion);

}
