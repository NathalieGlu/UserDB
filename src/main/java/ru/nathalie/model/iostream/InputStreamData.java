package ru.nathalie.model.iostream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamData {
    private String mapping;
    private String args;

    public InputStreamData(InputStream is) throws IOException {
        parseInput(is);
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

    private String readInput(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        StringBuilder headers = new StringBuilder();
        while (true) {
            String line = br.readLine();
            if (line == null || line.trim().length() == 0) {
                return headers.toString();
            }
            headers.append(line);
        }
    }

    private void parseInput(InputStream is) throws IOException {
        String data = readInput(is);

        String request = data.substring(data.indexOf(" ") + 1, data.indexOf(" ", data.indexOf(" ") + 1));
        if (request.contains("?")) {
            setMapping(request.substring(0, request.indexOf("?")));
            setArgs(request.substring(request.indexOf("?"), request.length()));
        } else {
            setMapping(request);
            setArgs(null);
        }
    }
}
