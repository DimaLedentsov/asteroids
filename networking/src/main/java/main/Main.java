package main;
import java.io.*;
import java.util.concurrent.Executors;

import net.client.Client;
import net.common.Request;
import net.common.Response;
import net.exceptions.ConnectionException;
import net.server.*;
import net.logwrapper.*;
public class Main {
    public static void main(String[] args) throws ConnectionException{
        System.out.println("Hello World!");
        Logger logger = new Log4jLogger("");
        Server<Request,Response> server = new Server<>(8080);
        server.setLogger(logger);
        server.setDataHandler((req)->{
            logger.info(req.getMessage());
            return new Response("server response");
        });
        server.start();

      
        Client<Response,Request> client = new Client<>(4040,"localhost",8080);
        client.setLogger(logger);
        client.setDataHandler((res)->{
            logger.info(res.getMessage());
            return new Request("client request");
        });
        client.start();

        client.send(new Request("first request"));

        Executors.newScheduledThreadPool(1).schedule(()->{
            System.out.println("done");
            server.close();
            client.close();
            System.exit(0);
        }, 5, java.util.concurrent.TimeUnit.SECONDS);

    }
}
