package org.nampython.center.dispatcher;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.SoletConfig;
import org.nampython.center.dispatcher.services.SoletConfigImpl;
import org.nampython.center.dispatcher.services.SoletConstants;
import org.nampython.config.JavacheConfigService;
import org.nampython.log.LoggingService;
import org.nampython.support.IoC;
import org.nampython.support.RequestHandler;
import org.nampython.support.RequestHandlerSharedData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Service
public class SoletDispatcher implements RequestHandler {
    private final SoletCandidateFinder soletCandidateFinder;
    private final SessionManagementService sessionManagementService;
    private final JavacheConfigService configService;
    private final ApplicationLoadingService applicationLoadingService;
    private final LoggingService loggingService;

    @Autowired
    public SoletDispatcher(SoletCandidateFinder soletCandidateFinder, SessionManagementService sessionManagementService, JavacheConfigService configService, ApplicationLoadingService applicationLoadingService, LoggingService loggingService) {
        this.soletCandidateFinder = soletCandidateFinder;
        this.sessionManagementService = sessionManagementService;
        this.configService = configService;
        this.applicationLoadingService = applicationLoadingService;
        this.loggingService = loggingService;
    }

    @Override
    public void init() {
        try {
            this.loggingService.info("Initializing Solet Dispatcher");
            this.soletCandidateFinder.init(
                    this.applicationLoadingService.loadApplications(this.createSoletConfig()),
                    this.applicationLoadingService.getApplicationNames()
            );
            this.loggingService.info("Loaded Applications: " + String.join(", ", this.applicationLoadingService.getApplicationNames()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean handleRequest(InputStream inputStream, OutputStream responseStream, RequestHandlerSharedData sharedData) throws IOException {
        return false;
    }

    @Override
    public int order() {
        return 0;
    }


    /**
     * Create SoletConfig instance and add objects.
     * This Solet Config will be used for initializing every solet.
     */
    private SoletConfig createSoletConfig() {
        final SoletConfig soletConfig = new SoletConfigImpl();
        soletConfig.setAttribute(
                SoletConstants.SOLET_CONFIG_SESSION_STORAGE_KEY,
                this.sessionManagementService.getSessionStorage()
        );

        soletConfig.setAttribute(
                SoletConstants.SOLET_CONFIG_SERVER_CONFIG_SERVICE_KEY,
                this.configService
        );

        soletConfig.setAttribute(
                SoletConstants.SOLET_CONFIG_DEPENDENCY_CONTAINER_KEY,
                IoC.getRequestHandlersDependencyContainer()
        );

        return soletConfig;
    }
}
