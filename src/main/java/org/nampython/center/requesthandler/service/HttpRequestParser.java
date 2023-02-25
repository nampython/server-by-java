package org.nampython.center.requesthandler.service;

import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.requesthandler.exception.CannotParseRequestException;

import java.io.InputStream;

public interface HttpRequestParser {
    HttpRequest parseHttpRequest(InputStream inputStream) throws CannotParseRequestException;
}
