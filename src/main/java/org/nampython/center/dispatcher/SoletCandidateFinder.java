package org.nampython.center.dispatcher;



import org.nampython.center.dispatcher.services.HttpSolet;
import org.nampython.center.dispatcher.services.HttpSoletRequest;

import java.util.List;
import java.util.Map;

public interface SoletCandidateFinder {

    void init(Map<String, HttpSolet> soletMap, List<String> applicationNames);

    HttpSolet findSoletCandidate(HttpSoletRequest request);
}
