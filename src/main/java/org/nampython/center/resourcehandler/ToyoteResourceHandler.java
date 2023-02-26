package org.nampython.center.resourcehandler;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
import org.nampython.log.LoggingService;
import org.nampython.support.RequestHandler;
import org.nampython.support.RequestHandlerSharedData;

import java.io.*;


/**
 * Request handler responsible for serving static resources such as images, html files, css, js and so on.
 */
@Service
public class ToyoteResourceHandler implements RequestHandler {
    private final JavacheConfigService configService;
    private final ResourceLocationService resourceLocationService;
    private final LoggingService loggingService;
    private final ResponsePopulationService responsePopulationService;

    @Autowired
    public ToyoteResourceHandler(JavacheConfigService configService, ResourceLocationService resourceLocationService, LoggingService loggingService, ResponsePopulationService responsePopulationService) {
        this.configService = configService;
        this.resourceLocationService = resourceLocationService;
        this.loggingService = loggingService;
        this.responsePopulationService = responsePopulationService;
    }

    @Override
    public void init() {
        this.responsePopulationService.init();

    }

    @Override
    public boolean handleRequest(InputStream inputStream, OutputStream outputStream, RequestHandlerSharedData sharedData) throws IOException {
        final HttpRequest request = sharedData.getObject(SharedDataPropertyNames.HTTP_REQUEST, HttpRequest.class);
        final HttpResponse response = sharedData.getObject(SharedDataPropertyNames.HTTP_RESPONSE, HttpResponse.class);

        try {
            final File resource = this.resourceLocationService.locateResource(request.getRequestURL());

            try (final FileInputStream fileInputStream = new FileInputStream(resource)) {
                this.responsePopulationService.handleResourceFoundResponse(request, response, resource, fileInputStream.available());
                outputStream.write(response.getBytes());
                this.transferStream(fileInputStream, outputStream);
            }

            return true;
        } catch (ResourceNotFoundException ignored) {
        }
        return false;
    }

    /**
     * The order of this request handler is configurable by the user.
     *
     * @return order of the request handler.
     */
    @Override
    public int order() {
        return this.configService.getConfigParam(JavacheConfigValue.TOYOTE_RESOURCE_HANDLER_ORDER, int.class);
    }

    private void transferStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        final byte[] buffer = new byte[2048];
        int read;

        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
    }

}
