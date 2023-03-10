package org.nampython.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class Reader {

    public Reader() {

    }

    public String readAllLines(InputStream inputStream) throws IOException {
        return new String(this.readAllBytes(inputStream), StandardCharsets.UTF_8);
    }

    public byte[] readAllBytes(InputStream inputStream) throws IOException {
        return inputStream.readAllBytes();
    }
}