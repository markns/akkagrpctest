package com.mns.akkagrpctest;


import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

import java.util.concurrent.CompletionStage;


public class DataServer {
    public static void main(String[] args) throws Exception {
        // important to enable HTTP/2 in ActorSystem's config
        Config conf = ConfigFactory.parseString("akka.http.server.preview.enable-http2 = on")
                .withFallback(ConfigFactory.defaultApplication());

        // Akka ActorSystem Boot
        ActorSystem sys = ActorSystem.create("HelloWorld", conf);

        run(sys).thenAccept(binding -> {
            System.out.println("gRPC server bound to: " + binding.localAddress());
        });

        // ActorSystem threads will keep the app alive until `system.terminate()` is called
    }

    private static CompletionStage<ServerBinding> run(ActorSystem sys) throws Exception {
        Materializer mat = ActorMaterializer.create(sys);

        // Instantiate implementation
        DataService impl = new DataServiceImpl(mat);

        return Http.get(sys).bindAndHandleAsync(
                DataServiceHandlerFactory.create(impl, mat, sys),
                ConnectHttp.toHost("127.0.0.1", 8080),
                mat);
    }


}
