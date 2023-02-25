package org.nampython.center.requesthandler;

import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.requesthandler.exception.CannotParseRequestException;

import java.io.InputStream;

/**
 * Service for reading request's body, parsing it using the multipart/form-data format
 * and populating {@link HttpRequest}
 */
public interface FormDataParser {

    /**
     * @param inputStream - request's input stream, read to the point where the body starts.
     * @param request     - current request.
     */
    void parseBodyParams(InputStream inputStream, HttpRequest request) throws CannotParseRequestException;
}
