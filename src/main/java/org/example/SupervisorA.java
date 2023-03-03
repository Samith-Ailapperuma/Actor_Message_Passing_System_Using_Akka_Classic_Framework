package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

public class SupervisorA extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        createActors();
    }

    public void createActors(){
        getContext().actorOf(Props.create(ActorOne.class),"ActorOne");
        System.out.println("ActorOne has been created");
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(Message.class, this::messageHandler)
                .build();
    }

    public void messageHandler(Message message){
        ActorRef sender = getContext().getSender();
        ActorRef mainSupervisor = getContext().getParent();
        ActorRef actorOne = getContext().findChild("ActorOne").get();

        if (sender == actorOne){
            message.setReceiver(mainSupervisor);
            message.setSender(getContext().self());

            message.messageScheduler(getContext(), message, mainSupervisor, getSelf(), 5000L);

        } else if (sender == mainSupervisor) {
            message.setReceiver(actorOne);
            message.setSender(getSelf());

            message.messageScheduler(getContext(), message, actorOne, getSelf(), 5000L);
        } else{
            System.out.println("Sender not defined at " + getSelf().path().name());
        }

    }

}
