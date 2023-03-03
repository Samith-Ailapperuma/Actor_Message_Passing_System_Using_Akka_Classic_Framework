package org.example;

import akka.actor.ActorContext;
import akka.actor.ActorRef;

import java.time.Duration;

public class Message{

    private ActorRef sender;
    private ActorRef receiver;

    public Message(ActorRef receiver, ActorRef sender){
        this.sender = sender;
        this.receiver = receiver;
    }

    public void createText(){
        try{

            String receiverName = getReceiver().path().name();
            String senderName = getSender().path().name();

            if (receiverName != null || senderName != null) {
                System.out.println("A message was sent by " + senderName + " to " + receiverName);
            }

        }catch (NullPointerException e){
            System.out.println("All actors have not been defined yet");
        }
    }

    public ActorRef getReceiver() {
        return receiver;
    }

    public void setReceiver(ActorRef receiver) {
        this.receiver = receiver;
    }

    public ActorRef getSender() {
        return sender;
    }

    public void setSender(ActorRef sender) {
        this.sender = sender;
    }

    public void messageScheduler(ActorContext actorContext, Message message, ActorRef receiver, ActorRef sender, Long duration){

        actorContext.system().scheduler().scheduleOnce(
                Duration.ofMillis(duration),
                receiver,
                message,
                actorContext.system().dispatcher(),
                sender);

        message.createText();

    }
}
