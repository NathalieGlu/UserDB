package ru.nathalie.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.model.iostream.OutputStreamData;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.io.OutputStream;

public class ExcHandler {

    private static final Logger log = LoggerFactory.getLogger(WebServer.class.getName());
    private OutputStream os;


    public void setStream(OutputStream os) {
        this.os = os;
    }

    public void printException(Throwable throwable) {
        OutputStreamData outputStreamData = new OutputStreamData();
        String message = throwable.getMessage();

        outputStreamData.setCode(message);
        outputStreamData.setContentLength(String.valueOf(message.length()));
        outputStreamData.setMessage(message);

        log.error("Got an exception, printing it: ", throwable);
        writeResponse(outputStreamData.getBody());
    }

    private void writeResponse(String response) {
        try {
            os.write(response.getBytes());
            os.flush();
        } catch (IOException e) {
            log.error("Got an exception, printing: ", e);
        }
    }
}
