package ru.nathalie.model.iostream;

public class OutputStreamData {
    private String httpVersion = "HTTP/1.1 ";
    private String code;
    private String serverInfo = "Server: NathalieGlu/2018-03-05\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    private String message = "\r\nConnection: close\r\n\r\n";

    public void setCode(String code) {
        this.code = code;
    }

    public void setContentLength(String ContentLength) {
        this.serverInfo += ContentLength;
    }

    public void setMessage(String message) {
        this.message += message;
    }

    public String getBody() {
        return httpVersion + code + serverInfo + message;
    }
}
