package org.nampython.center.requesthandler.lamda;


import org.nampython.center.requesthandler.exception.CannotParseRequestException;

@FunctionalInterface
public interface MultipartParserErrorCallback {
    void onError(Throwable throwable) throws CannotParseRequestException;
}
