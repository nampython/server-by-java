package org.nampython.center.resourcehandler;



import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;

import java.io.File;
import java.io.IOException;

public interface ResponsePopulationService {
    void init();
    void handleResourceFoundResponse(HttpRequest request, HttpResponse response, File resourceFile, long fileSize) throws IOException;
}
