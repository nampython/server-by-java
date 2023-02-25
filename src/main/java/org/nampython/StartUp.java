package org.nampython;

import com.cyecize.ioc.MagicInjector;
import com.cyecize.ioc.services.DependencyContainer;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
import org.nampython.support.IoC;
import org.nampython.util.WebConstants;

public class StartUp {

    /**
     * Server entry point.
     * Runs {@link MagicInjector}.
     *
     * @param args - first argument is the server port.
     */
    public static void main(String[] args) {
//        final DependencyContainer dependencyContainer = MagicInjector.run(
//                StartUp.class,
//                WebConstants.JAVACHE_IOC_CONFIGURATION
//        );
//
//        IoC.setJavacheDependencyContainer(dependencyContainer);
//
//        dependencyContainer.getService(JavacheConfigService.class)
//                .addConfigParam(JavacheConfigValue.SERVER_STARTUP_ARGS, args);
//
//        dependencyContainer.getService(ServerInitializer.class).initializeServer();
    }
}
