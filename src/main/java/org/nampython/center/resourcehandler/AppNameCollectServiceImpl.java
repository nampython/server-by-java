package org.nampython.center.resourcehandler;

import com.cyecize.ioc.annotations.Autowired;
import org.nampython.annotation.JavacheComponent;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
import org.nampython.util.PathUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@JavacheComponent
public class AppNameCollectServiceImpl implements AppNameCollectService {
    private static final String INVALID_WEB_APPS_DIR_FORMAT = "Directory \"%s\" is not a valid webapps directory!";
    private static final String JAR_FILE_EXTENSION = ".jar";
    private final JavacheConfigService configService;

    @Autowired
    public AppNameCollectServiceImpl(JavacheConfigService configService) {
        this.configService = configService;
    }

    /**
     * Iterates the webapps dir and collects the name of all JAR files.
     * @return list containing all loaded web apps.
     */
    @Override
    public List<String> getApplicationNames() {
        final String webappsDir = PathUtils.appendPath(
                this.configService.getConfigParamString(JavacheConfigValue.JAVACHE_WORKING_DIRECTORY),
                this.configService.getConfigParamString(JavacheConfigValue.WEB_APPS_DIR_NAME)
        );

        final File webappsFolder = new File(webappsDir);
        if (!webappsFolder.exists() || !webappsFolder.isDirectory()) {
            throw new RuntimeException(String.format(INVALID_WEB_APPS_DIR_FORMAT, webappsDir));
        }

        final List<String> appNames = new ArrayList<>();

        for (File file : webappsFolder.listFiles()) {
            if (!file.isFile() || !file.getName().endsWith(JAR_FILE_EXTENSION)) continue;

            appNames.add(file.getName().replace(JAR_FILE_EXTENSION, ""));
        }

        return appNames;
    }
}
