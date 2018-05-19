package io.choerodon.config.domain;

import java.util.Date;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * feign调用manager服务获取的服务配置信息实体
 * @author crock
 */
public class Config {

    @JsonIgnore
    private Date publicTime;

    private Long id;

    private String name;

    private String configVersion;

    private Boolean isDefault;

    private Long serviceId;

    private Map<String, Object> value;

    private String source;

    private Long objectVersionNumber;

    public Date getPublicTime() {
        return publicTime;
    }

    public void setPublicTime(Date publicTime) {
        this.publicTime = publicTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Long getServiceId() {
        return serviceId;
    }

    public void setServiceId(Long serviceId) {
        this.serviceId = serviceId;
    }

    public Map<String, Object> getValue() {
        return value;
    }

    public void setValue(Map<String, Object> value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Long getObjectVersionNumber() {
        return objectVersionNumber;
    }

    public void setObjectVersionNumber(Long objectVersionNumber) {
        this.objectVersionNumber = objectVersionNumber;
    }

    @Override
    public String toString() {
        return "Config{"
                + "publicTime=" + publicTime
                + ", id=" + id
                + ", name='" + name + '\''
                + ", configVersion='" + configVersion + '\''
                + ", isDefault=" + isDefault
                + ", serviceId=" + serviceId
                + ", value=" + value
                + ", source='" + source + '\''
                + ", objectVersionNumber=" + objectVersionNumber
                + '}';
    }
}
