package org.nampython.center.dispatcher.services.api;

import java.io.InputStream;

public interface MultipartFile {

    long getFileLength();

    String getContentType();

    String getFileName();

    String getFieldName();

    InputStream getInputStream();

    byte[] getBytes();
}
