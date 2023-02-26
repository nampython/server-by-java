package org.nampython.center.resourcehandler;


import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;

public interface CacheControlService {
    void init();

    void addCachingHeader(HttpRequest request, HttpResponse response, String fileMediaType);
}
