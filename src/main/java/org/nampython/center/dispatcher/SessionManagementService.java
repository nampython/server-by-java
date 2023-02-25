package org.nampython.center.dispatcher;


import org.nampython.center.dispatcher.services.HttpSoletRequest;
import org.nampython.center.dispatcher.services.api.HttpResponse;
import org.nampython.center.dispatcher.services.api.HttpSessionStorage;

public interface SessionManagementService {

    void initSessionIfExistent(HttpSoletRequest request);

    void sendSessionIfExistent(HttpSoletRequest request, HttpResponse response);

    void clearInvalidSessions();

    HttpSessionStorage getSessionStorage();
}
