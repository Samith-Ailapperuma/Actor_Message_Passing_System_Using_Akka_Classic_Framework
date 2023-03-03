package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;

public class ActorTwo extends AbstractActor {

    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class,this::onReceive)
                .build();
    }

    public void onReceive(Message message){

        ActorRef senderActor = message.getSender();

        resendMessage(message, senderActor);
    }

    public void resendMessage(Message message, ActorRef previousSender){
        System.out.println();
        message.setSender(getContext().self());
        message.setReceiver(previousSender);

        message.messageScheduler(getContext(), message, previousSender, getSelf(), 5000L);
    }

}
