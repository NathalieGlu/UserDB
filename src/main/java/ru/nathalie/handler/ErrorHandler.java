package ru.nathalie.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.model.data.ResponseData;

public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class.getName());

    public String getException(Throwable throwable) {
        ResponseData responseData = new ResponseData();
        String message = throwable.getMessage();

        responseData.setCode(message);
        responseData.setContentLength(String.valueOf(message.length()));
        responseData.setMessage(message);

        log.error("Got an exception, printing it: ", throwable);
        return responseData.getBody();
    }
}
