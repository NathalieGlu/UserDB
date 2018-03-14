package ru.nathalie.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.config.AppProperties;
import ru.nathalie.factory.ServerFactory;
import ru.nathalie.web.NettyServer;

import java.io.IOException;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class.getName());
    private static ServerFactory factory = new ServerFactory();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException, InterruptedException {
        AppProperties properties = (AppProperties) factory.setClass(AppProperties.class);
        NettyServer server = new NettyServer(properties.getPort());
        server.run();
    }
}
