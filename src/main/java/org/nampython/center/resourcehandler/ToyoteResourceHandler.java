package org.nampython.center.resourcehandler;

import org.nampython.support.RequestHandler;
import org.nampython.support.RequestHandlerSharedData;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ToyoteResourceHandler implements RequestHandler {

    @Override
    public void init() {

    }

    @Override
    public boolean handleRequest(InputStream inputStream, OutputStream responseStream, RequestHandlerSharedData sharedData) throws IOException {
        return false;
    }

    @Override
    public int order() {
        return 0;
    }
}
