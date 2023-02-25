
package org.nampython;


import com.cyecize.ioc.MagicInjector;
import com.cyecize.ioc.config.MagicConfiguration;
import com.cyecize.ioc.services.DependencyContainer;
import org.nampython.annotation.JavacheEmbeddedComponent;
import org.nampython.config.JavacheConfigBeanCreator;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
import org.nampython.log.LoggingService;
import org.nampython.server.Server;
import org.nampython.server.ServerImpl;
import org.nampython.support.IoC;
import org.nampython.support.RequestHandlerLoadingService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServerInitialization {

    public static void startServer(Integer port, Class<?> serverInitializationClass) {
        startServer(port, new HashMap<>(), serverInitializationClass);

    }

    public static void startServer(Integer port, Map<String, Object> config, Class<?> mainClass) {
        startServer(port, config, mainClass, null);
    }

    private static void startServer(Integer port, Map<String, Object> config, Class<?> serverInitializationClass, Runnable onServerLoadedEvent) {
        LoggingService loggingService = null;

        try {
            final MagicConfiguration magicConfiguration = new MagicConfiguration()
                    .scanning().addCustomServiceAnnotation(JavacheEmbeddedComponent.class)
                    .and()
                    .build();
            JavacheConfigBeanCreator.config = config;
            JavacheConfigBeanCreator.mainClass = serverInitializationClass;
            JavacheConfigBeanCreator.port = port;


            final DependencyContainer dependencyContainer = MagicInjector.run(ServerInitialization.class, magicConfiguration);
            IoC.setJavacheDependencyContainer(dependencyContainer);
            IoC.setRequestHandlersDependencyContainer(dependencyContainer);
            loggingService = dependencyContainer.getService(LoggingService.class);

            dependencyContainer.getService(RequestHandlerLoadingService.class).loadRequestHandlers(
                    new ArrayList<>(), null, null
            );

            final Server server = new ServerImpl(
                    dependencyContainer.getService(JavacheConfigService.class).getConfigParam(JavacheConfigValue.SERVER_PORT, int.class),
                    dependencyContainer.getService(LoggingService.class),
                    dependencyContainer.getService(RequestHandlerLoadingService.class)
            );


            if (onServerLoadedEvent != null) {
                onServerLoadedEvent.run();
            }
            server.run();
        } catch (Exception ex) {
            if (loggingService != null) {
                loggingService.printStackTrace(ex);
            } else {
                ex.printStackTrace();
            }
            System.exit(1);
        }
    }
}
