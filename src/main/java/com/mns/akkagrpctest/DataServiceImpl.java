package com.mns.akkagrpctest;

import akka.NotUsed;
import akka.stream.Materializer;
import akka.stream.javadsl.Source;

import java.util.List;
import java.util.stream.Collectors;

public class DataServiceImpl implements DataService {

    private final Materializer mat;

    public DataServiceImpl(Materializer mat) {
        this.mat = mat;
    }

    public Source<RowSet, NotUsed> subscribe(DataRequest in) {

        System.out.println("sayHello to " + in.getName() + " with stream of chars");
        List<Character> characters = ("Hello, " + in.getName())
                .chars().mapToObj(c -> (char) c).collect(Collectors.toList());
        return Source.from(characters)
                .map(character -> {
                    return HelloReply.newBuilder().setMessage(String.valueOf(character)).build();
                });
    }
}
