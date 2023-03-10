package org.nampython.config;

import java.util.List;
import java.util.Map;

public interface JavacheConfigService {

    void addConfigParam(String paramName, Object value);

    void addConfigParam(JavacheConfigValue paramName, Object value);

    <T> T getConfigParam(String paramName, Class<T> type);

    <T> T getConfigParam(JavacheConfigValue paramName, Class<T> type);

    Object getConfigParam(JavacheConfigValue paramName);

    String getConfigParamString(String paramName);

    String getConfigParamString(JavacheConfigValue paramName);

    Map<String, Object> getConfigParams();

    List<String> getRequestHandlerPriority();
}
