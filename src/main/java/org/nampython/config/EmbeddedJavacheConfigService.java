package org.nampython.config;

import java.util.Map;

public class EmbeddedJavacheConfigService extends JavacheConfigServiceImpl {

    public EmbeddedJavacheConfigService(Map<String, Object> config) {
        super();
        this.setConfig(config);
    }

    /**
     * Leave blank since request handlers are pre-defined.
     */
    @Override
    protected void loadRequestHandlerConfig() {
        //do nothing
    }

    /**
     * transfer runtime config map to global map.
     */
    private void setConfig(Map<String, Object> config) {
        super.configParameters.putAll(config);
    }
}
