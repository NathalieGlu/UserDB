package ru.nathalie.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.model.dto.ResponseDto;

public class ErrorHandler {
    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class.getName());

    public String getException(Throwable throwable) {
        ResponseDto responseDto = new ResponseDto();
        String message = throwable.getMessage();

        responseDto.setCode(message);
        responseDto.setContentLength(String.valueOf(message.length()));
        responseDto.setMessage(message);

        log.error("Got an exception, printing it: ", throwable);
        return responseDto.getBody();
    }

    public String getException(Throwable throwable, String error) {
        ResponseDto responseDto = new ResponseDto();
        String message = throwable.getMessage();

        responseDto.setCode(message);
        responseDto.setCode(String.valueOf(error.length()));
        responseDto.setMessage(error);

        log.error("Got an exception, printing it: ", throwable);
        return responseDto.getBody();
    }
}
