package ru.nathalie.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import ru.nathalie.factory.ServerFactory;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ServletTest extends Mockito {

    @Mock
    WebServer webServer;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUsers() {

        WebServer server = ServerFactory.build();
        String result = server.getUsers();
        System.out.println("Result: " + result);

        when(webServer.getUsers()).thenReturn(result);

        assertEquals(result, webServer.getUsers());
    }

    @Test
    public void testGetBalance() {
        WebServer server = ServerFactory.build();
        int id = 1452135;

        String balance = server.getBalance(id);
        System.out.println("Balance: " + balance);

        when(webServer.getBalance(id)).thenReturn(balance);

        assertEquals(balance, webServer.getBalance(id));
    }
}