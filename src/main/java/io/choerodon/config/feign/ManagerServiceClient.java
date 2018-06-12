package io.choerodon.config.feign;

import io.choerodon.config.domain.Config;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    @RequestMapping(value = "/v1/configs/{service_name}/configs/default", method = RequestMethod.GET)
    Config getConfig(@PathVariable("service_name") String serviceName);

    /**
     * 向配置管理服务索要配置
     * @param serviceName 应用名
     * @param configVersion 配置版本
     * @return 配置集
     */
    @RequestMapping(value = "/v1/configs/{service_name}/configs/{config_version}", method = RequestMethod.GET)
    Config getConfig(@PathVariable("service_name") String serviceName,  @PathVariable("config_version") String configVersion);

}
