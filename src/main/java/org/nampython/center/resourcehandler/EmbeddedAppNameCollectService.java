package org.nampython.center.resourcehandler;

import com.cyecize.ioc.annotations.Autowired;
import org.nampython.annotation.JavacheEmbeddedComponent;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;

import java.util.List;

@JavacheEmbeddedComponent
public class EmbeddedAppNameCollectService implements AppNameCollectService {

    private final JavacheConfigService configService;

    @Autowired
    public EmbeddedAppNameCollectService(JavacheConfigService configService) {
        this.configService = configService;
    }

    @Override
    public List<String> getApplicationNames() {
        return List.of(this.configService.getConfigParamString(JavacheConfigValue.MAIN_APP_JAR_NAME));
    }
}
