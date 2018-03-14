package ru.nathalie.model.data;

import java.io.IOException;

public class RequestData {
    private String mapping;
    private String args;

    public RequestData(String requestString) throws IOException {
        parseInput(requestString);
    }

    public String getMapping() {
        return mapping;
    }

    private void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getArgs() {
        return args;
    }

    private void setArgs(String args) {
        this.args = args;
    }

    private void parseInput(String requestString) throws IOException {
        String request = requestString.substring(requestString.indexOf(" ") + 1, requestString.indexOf(" ", requestString.indexOf(" ") + 1));
        if (request.contains("?")) {
            setMapping(request.substring(0, request.indexOf("?")));
            setArgs(request.substring(request.indexOf("?"), request.length()));
        } else {
            setMapping(request);
            setArgs(null);
        }
    }

}
