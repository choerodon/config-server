package io.choerodon.config.domain;

import java.util.Map;

/**
 * feign调用manager服务获取的服务配置信息实体
 * @author crock
 */
public class Config {

    private String name;

    private String configVersion;

    private Boolean isDefault;

    private Map<String, Object> value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigVersion() {
        return configVersion;
    }

    public void setConfigVersion(String configVersion) {
        this.configVersion = configVersion;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "Config{" +
                "name='" + name + '\'' +
                ", configVersion='" + configVersion + '\'' +
                ", isDefault=" + isDefault +
                ", value=" + value +
                '}';
    }
}
