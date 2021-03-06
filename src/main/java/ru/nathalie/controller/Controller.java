package ru.nathalie.controller;

import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dto.ResponseDto;

class Controller {
    static final String HTTP_NOT_FOUND = "404 Not Found\n";
    static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    static final String HTTP_CONFLICT = "409 Conflict\n";
    private static final String HTTP_OK = "200 OK\n";
    private final ErrorHandler handler;

    Controller(ErrorHandler handler) {
        this.handler = handler;
    }

    String throwException(Throwable throwable) {
        return handler.getException(throwable);
    }

    String throwException(Throwable throwable, String error) {
        return handler.getException(throwable, error);
    }

    String sendOK(String data) {
        ResponseDto os = new ResponseDto();
        os.setCode(HTTP_OK);
        os.setContentLength(String.valueOf(data.length()));
        os.setMessage(data);
        return os.getBody();
    }
}
