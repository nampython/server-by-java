package org.nampython.center.resourcehandler;

import java.io.File;

public interface ResourceLocationService {
    File locateResource(String requestURL) throws ResourceNotFoundException;
}
