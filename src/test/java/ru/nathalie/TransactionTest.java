package ru.nathalie;

import org.junit.Test;
import ru.nathalie.web.NettyClient;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

public class TransactionTest {

    @Test
    public void testClient() throws Exception {
        String test1 = "POST /transaction?from=1&to=2&amount=1 HTTP/1.1";
        ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Collection<Future<?>> futures = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            futures.add(threadPool.submit(new NettyClient(test1)));
        }
        for (Future<?> future : futures) {
            future.get();
        }
        threadPool.shutdown();
    }

}