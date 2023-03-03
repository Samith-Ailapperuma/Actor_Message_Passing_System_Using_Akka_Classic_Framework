package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class SupervisorB extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        createActors();
    }

    public void createActors(){
        getContext().actorOf(Props.create(ActorTwo.class),"ActorTwo");
        System.out.println("ActorTwo has been created");
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(Message.class, this::messageHandler)
                .build();
    }

    public void messageHandler(Message message){
        ActorRef sender = message.getSender();
        ActorRef mainSupervisor = getContext().getParent();
        ActorRef actorTwo = getContext().findChild("ActorTwo").get();
        
        if(sender == mainSupervisor){
            message.setReceiver(actorTwo);
            message.setSender(getSelf());

            message.messageScheduler(getContext(), message, actorTwo, getSelf(), 5000L);
        } else if (sender == actorTwo) {
            message.setSender(getSelf());
            message.setReceiver(mainSupervisor);

            message.messageScheduler(getContext(), message, mainSupervisor, getSelf(), 5000L);
        } else {
            System.out.println("Sender not defined at " + getSelf().path().name());
        }

    }
}
