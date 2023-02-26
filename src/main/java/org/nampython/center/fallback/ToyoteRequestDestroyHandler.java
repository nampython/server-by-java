package org.nampython.center.fallback;

import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.MultipartFile;
import org.nampython.center.resourcehandler.SharedDataPropertyNames;
import org.nampython.config.RequestDestroyHandler;
import org.nampython.support.RequestHandlerSharedData;

import java.io.IOException;

/**
 * Request handler called always after every request.
 * The purpose is to clear or dispose any left out resource to avoid memory leaks.
 */
@Service
public class ToyoteRequestDestroyHandler implements RequestDestroyHandler {

    @Override
    public void destroy(RequestHandlerSharedData sharedData) {
        final HttpRequest request = sharedData.getObject(SharedDataPropertyNames.HTTP_REQUEST, HttpRequest.class);
        if (request == null || request.getMultipartFiles() == null) {
            return;
        }

        for (MultipartFile multipartFile : request.getMultipartFiles()) {
            try {
                multipartFile.getInputStream().close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
