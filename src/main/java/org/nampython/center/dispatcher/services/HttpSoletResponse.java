package org.nampython.center.dispatcher.services;

import org.nampython.center.dispatcher.services.api.HttpResponse;

public interface HttpSoletResponse extends HttpResponse {

    void sendRedirect(String location);

    SoletOutputStream getOutputStream();
}
