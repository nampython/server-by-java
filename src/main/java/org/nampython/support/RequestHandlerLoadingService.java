package org.nampython.support;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

public interface RequestHandlerLoadingService {
    void loadRequestHandlers(List<String> requestHandlerFileNames, Map<File, URL> libURLs, Map<File, URL> apiURLs);
    List<RequestHandler> getRequestHandlers();
    List<RequestDestroyHandler> getRequestDestroyHandlers();
}
