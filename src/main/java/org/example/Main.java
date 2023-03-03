package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("system");

        actorSystem.actorOf(Props.create(MainSupervisor.class), "MainSupervisor");

        System.out.println("Main supervisor has been created");
    }
}