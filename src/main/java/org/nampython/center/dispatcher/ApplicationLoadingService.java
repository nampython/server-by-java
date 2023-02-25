package org.nampython.center.dispatcher;



import org.nampython.center.dispatcher.services.HttpSolet;
import org.nampython.center.dispatcher.services.SoletConfig;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApplicationLoadingService {
    List<String> getApplicationNames();
    Map<String, HttpSolet> loadApplications(SoletConfig soletConfig) throws IOException;
}
