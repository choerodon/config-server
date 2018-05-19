package io.choerodon.config.feign;

import io.choerodon.config.domain.Config;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
    @RequestMapping(value = "/v1/configs/{service_name}/default", method = RequestMethod.GET)
    Config getConfig(@PathVariable("service_name") String serviceName);

    /**
     * 向配置管理服务索要配置
     * @param serviceName 应用名
     * @param configVersion 配置版本
     * @return 配置集
     */
    @RequestMapping(value = "/v1/configs/{serviceName}/by_version", method = RequestMethod.GET)
    Config getConfig(@PathVariable("serviceName") String serviceName,  @RequestParam("config_version") String configVersion);

}
