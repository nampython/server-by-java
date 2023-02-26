package org.nampython.center.requesthandler;


import java.util.Arrays;

import static org.nampython.center.requesthandler.ToyoteConstants.TEXT_PLAIN;

public enum FormDataParserProvider {
    DEFAULT(TEXT_PLAIN, FormDataParserDefaultImpl.class);
//    MULTIPART(MULTIPART_FORM_DATA, FormDataParserMultipartImpl.class);

    private final String contentType;
    private final Class<? extends FormDataParser> parserType;

    FormDataParserProvider(String contentType, Class<? extends FormDataParser> parserType) {
        this.contentType = contentType;
        this.parserType = parserType;
    }

    public Class<? extends FormDataParser> getParserType() {
        return this.parserType;
    }

    public static FormDataParserProvider findByContentType(String contentType) {
        if (contentType == null) {
            return DEFAULT;
        }

        return Arrays.stream(values())
                .filter(provider -> contentType.startsWith(provider.contentType))
                .findFirst().orElse(DEFAULT);
    }

}
