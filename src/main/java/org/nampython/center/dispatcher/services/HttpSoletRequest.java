package org.nampython.center.dispatcher.services;


import org.nampython.center.dispatcher.services.api.HttpRequest;

public interface HttpSoletRequest extends HttpRequest {

    void setContextPath(String contextPath);

    String getContextPath();

    String getRelativeRequestURL();
}
