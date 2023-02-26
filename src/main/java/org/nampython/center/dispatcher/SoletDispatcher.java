package org.nampython.center.dispatcher;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.*;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;
import org.nampython.center.dispatcher.services.api.HttpStatus;
import org.nampython.center.resourcehandler.SharedDataPropertyNames;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
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

    private final boolean trackResources;


    @Autowired
    public SoletDispatcher(SoletCandidateFinder soletCandidateFinder, SessionManagementService sessionManagementService, JavacheConfigService configService, ApplicationLoadingService applicationLoadingService, LoggingService loggingService) {
        this.soletCandidateFinder = soletCandidateFinder;
        this.sessionManagementService = sessionManagementService;
        this.configService = configService;
        this.applicationLoadingService = applicationLoadingService;
        this.loggingService = loggingService;
        this.trackResources = configService.getConfigParam(JavacheConfigValue.BROCCOLINA_TRACK_RESOURCES, boolean.class);
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
    public boolean handleRequest(InputStream inputStream, OutputStream outputStream, RequestHandlerSharedData sharedData) throws IOException {
        final HttpSoletRequest request = new HttpSoletRequestImpl(
                sharedData.getObject(SharedDataPropertyNames.HTTP_REQUEST, HttpRequest.class)
        );

        final HttpSoletResponse response = new HttpSoletResponseImpl(
                sharedData.getObject(SharedDataPropertyNames.HTTP_RESPONSE, HttpResponse.class),
                outputStream
        );
        if (request.isResource() && !this.trackResources) {
            return false;
        }
        this.sessionManagementService.initSessionIfExistent(request);
        final HttpSolet solet = this.soletCandidateFinder.findSoletCandidate(request);

        if (solet == null || !this.runSolet(solet, request, response)) {
            return false;
        }
        if (response.getStatusCode() == null) {
            response.setStatusCode(HttpStatus.OK);
        }
        if (!response.getHeaders().containsKey(BroccolinaConstants.CONTENT_LENGTH_HEADER)
                && response.getContent() != null
                && response.getContent().length > 0) {
            response.addHeader(BroccolinaConstants.CONTENT_LENGTH_HEADER, response.getContent().length + "");
        }
        this.sessionManagementService.sendSessionIfExistent(request, response);
        this.sessionManagementService.clearInvalidSessions();
        response.getOutputStream().write();

        return true;
    }

    @Override
    public int order() {
        return (Integer)this.configService.getConfigParam(JavacheConfigValue.BROCCOLINA_SOLET_DISPATCHER_ORDER, Integer.TYPE);
    }


    private boolean runSolet(HttpSolet solet, HttpSoletRequest request, HttpSoletResponse response) {
        try {
            solet.service(request, response);
            return solet.hasIntercepted();
        } catch (Exception ex) {
            this.loggingService.printStackTrace(ex);
        }

        return true;
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
