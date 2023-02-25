package org.nampython.center.requesthandler.lamda;


import org.nampython.center.requesthandler.models.MultipartEntry;

@FunctionalInterface
public interface MultipartParserFieldParsedCallback {
    void onFieldParsed(MultipartEntry multipartEntry);
}
