package org.nampython.center.requesthandler;

import org.nampython.center.dispatcher.services.api.HttpResponse;
import org.nampython.center.dispatcher.services.api.HttpStatus;
import org.nampython.center.requesthandler.exception.RequestTooBigException;

import java.io.IOException;
import java.io.OutputStream;

public interface ErrorHandlingService {

    boolean handleRequestTooBig(OutputStream outputStream, RequestTooBigException ex, HttpResponse response) throws IOException;

    boolean handleException(OutputStream outputStream, Throwable throwable, HttpResponse response) throws IOException;

    boolean handleException(OutputStream outputStream, Throwable throwable, HttpResponse response, HttpStatus status) throws IOException;
}
