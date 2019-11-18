package com.mns.akkagrpctest;

import akka.NotUsed;
import akka.stream.Materializer;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Source;

import java.time.Duration;

public class DataServiceImpl implements DataService {

    private final Materializer mat;

    private Source<Object, NotUsed> source;

    // Inject Kafka client
    public DataServiceImpl(Materializer mat) {
        this.mat = mat;

        // replace with subscription to Kafka
        this.source = Source.tick(Duration.ofSeconds(1L), Duration.ofSeconds(1L), new Object())
                .map(o -> new Object(), Keep.left());

    }

    public Source<RowSet, NotUsed> subscribe(DataRequest in) {

//        System.out.println("sayHello to " + in.getName() + " with stream of chars");
//        List<Character> characters = ("Hello, " + in.getName())
//                .chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        return this.source
                .map(character -> {
                    return RowSet.newBuilder().addValue("yo").build();
                });
    }
}
