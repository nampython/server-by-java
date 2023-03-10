package org.nampython.center.requesthandler;

import com.cyecize.ioc.annotations.Autowired;
import com.cyecize.ioc.annotations.Service;
import org.nampython.center.dispatcher.services.api.HttpRequest;
import org.nampython.center.requesthandler.exception.CannotParseRequestException;
import org.nampython.config.JavacheConfigService;
import org.nampython.config.JavacheConfigValue;
import org.nampython.log.LoggingService;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
public class FormDataParserDefaultImpl implements FormDataParser {

    private final LoggingService loggingService;

    private final boolean showRequestLog;

    @Autowired
    public FormDataParserDefaultImpl(LoggingService loggingService,
                                     JavacheConfigService configService) {
        this.loggingService = loggingService;
        this.showRequestLog = configService.getConfigParam(JavacheConfigValue.SHOW_REQUEST_LOG, boolean.class);
    }

    @Override
    public void parseBodyParams(InputStream inputStream, HttpRequest request) throws CannotParseRequestException {
        try {
            this.setBodyParameters(this.readBody(inputStream, request), request);
        } catch (IOException e) {
            throw new CannotParseRequestException(e.getMessage(), e);
        }
    }

    private void setBodyParameters(String requestBody, HttpRequest request) {
        if (requestBody == null || requestBody.isEmpty() || requestBody.trim().isEmpty()) {
            return;
        }

        request.addBodyParameter(ToyoteConstants.RAW_BODY_PARAM_NAME, requestBody);

        final String[] bodyParamPairs = requestBody.split("&");

        for (String bodyParamPair : bodyParamPairs) {
            final String[] tokens = bodyParamPair.split("=");
            final String paramKey = decode(tokens[0]);
            final String value = tokens.length > 1 ? decode(tokens[1]) : null;

            request.addBodyParameter(paramKey, value);
        }
    }

    private String readBody(InputStream inputStream, HttpRequest request) throws IOException {
        final int contentLength = request.getContentLength();

        final byte[] bytes = inputStream.readNBytes(contentLength);

        final String body = new String(bytes, StandardCharsets.UTF_8);
        if (this.showRequestLog) {
            this.loggingService.info(body);
        }

        return body;
    }

    private static String decode(String str) {
        return URLDecoder.decode(str, StandardCharsets.UTF_8);
    }
}
