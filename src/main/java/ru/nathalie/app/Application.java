package ru.nathalie.app;

import ru.nathalie.config.AppProperties;
import ru.nathalie.factory.ServerFactory;
import ru.nathalie.web.NettyServer;

import java.io.IOException;

class Application {
    private static final ServerFactory factory = new ServerFactory();

    public static void main(String[] args) throws IOException, InterruptedException {
        AppProperties properties = (AppProperties) factory.setClass(AppProperties.class);
        NettyServer server = new NettyServer(properties.getPort());
        server.run();
    }
}
