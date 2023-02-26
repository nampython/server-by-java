package org.nampython.center.requesthandler;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;
import org.nampython.center.dispatcher.services.api.HttpResponseImpl;
import org.nampython.center.dispatcher.services.api.HttpStatus;
import org.nampython.center.requesthandler.exception.RequestTooBigException;
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
    private final ErrorHandlingService errorHandlingService;


    @Autowired
    public ToyoteRequestHandler(LoggingService loggingService, HttpRequestParser httpRequestParser, ErrorHandlingService errorHandlingService) {
        this.loggingService = loggingService;
        this.httpRequestParser = httpRequestParser;
        this.errorHandlingService = errorHandlingService;
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
    public boolean handleRequest(InputStream inputStream, OutputStream outputStream, RequestHandlerSharedData sharedData) throws IOException {
        try {
            loggingService.info("Start the request procesing in the " + ToyoteRequestHandler.class.getSimpleName());
            final HttpRequest httpRequest = this.httpRequestParser.parseHttpRequest(inputStream);
            final HttpResponse httpResponse = new HttpResponseImpl();
            sharedData.addObject("HTTP_REQUEST", httpRequest);
            sharedData.addObject("HTTP_RESPONSE", httpResponse);
        } catch (RequestTooBigException ex) {
            this.disposeInputStream(ex.getContentLength(), inputStream);
            return this.errorHandlingService.handleRequestTooBig(outputStream, ex, new HttpResponseImpl());
        } catch (Exception e) {
            return this.errorHandlingService.handleException(outputStream, e, new HttpResponseImpl(), HttpStatus.BAD_REQUEST);
        }
        return false;
    }

    /**
     * The purpose of this method is to read the input stream before closing it
     * otherwise the TCP connection will not be closed properly.
     */
    private void disposeInputStream(int length, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[0];
        int leftToRead = length;
        int bytesRead = Math.min(2048, inputStream.available());

        while (leftToRead > 0) {
            buffer = inputStream.readNBytes(bytesRead);
            leftToRead -= bytesRead;
            bytesRead = Math.min(2048, inputStream.available());
        }

        buffer = null;
    }

    @Override
    public int order() {
        return Integer.MIN_VALUE;
    }
}
