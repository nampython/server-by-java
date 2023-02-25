package org.nampython.center.requesthandler;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.requesthandler.service.HttpRequestParser;
import org.nampython.log.LoggingService;
import org.nampython.support.RequestHandler;
import org.nampython.support.RequestHandlerSharedData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Request handler responsible for reading and parsing the request input stream.
 * This request is always first to be executed.
 */
@Service
public class ToyoteRequestHandler implements RequestHandler {

   private final LoggingService loggingService;
    private final HttpRequestParser httpRequestParser;


    @Autowired
    public ToyoteRequestHandler(LoggingService loggingService, HttpRequestParser httpRequestParser) {
        this.loggingService = loggingService;
        this.httpRequestParser = httpRequestParser;
    }



    @Override
    public void init() {

    }

    /**
     *
     * @param inputStream    request stream.
     * @param responseStream response stream.
     * @param sharedData     POJO which lives for the duration of the request and can be used
     *                       to store and share data between request handlers.
     * @return
     * @throws IOException
     */
    @Override
    public boolean handleRequest(InputStream inputStream, OutputStream responseStream, RequestHandlerSharedData sharedData) throws IOException {
        loggingService.info("Start the request procesing in the " + ToyoteRequestHandler.class.getSimpleName());
        final HttpRequest httpRequest = this.httpRequestParser.parseHttpRequest(inputStream);
        return false;
    }

    @Override
    public int order() {
        return 0;
    }
}
