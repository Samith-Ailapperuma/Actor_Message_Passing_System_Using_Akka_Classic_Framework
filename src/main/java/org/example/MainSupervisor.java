package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.time.Duration;

public class MainSupervisor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        super.preStart();
        createSupervisors();
    }

    public void createSupervisors(){
        getContext().actorOf(Props.create(SupervisorA.class),"SupervisorA");
        System.out.println("SupervisorA has been created");

        getContext().actorOf(Props.create(SupervisorB.class), "SupervisorB");
        System.out.println("SupervisorB has been created");
    }

    @Override
    public Receive createReceive(){
        return receiveBuilder()
                .match(Message.class, this::sendMessage)
                .build();
    }

    public void sendMessage(Message message){
        ActorRef sender = message.getSender();
        ActorRef supervisorA = getContext().findChild("SupervisorA").get();
        ActorRef supervisorB = getContext().findChild("SupervisorB").get();

        if(sender == supervisorA){
            message.setSender(getSelf());
            message.setReceiver(supervisorB);

            message.messageScheduler(getContext(),message, supervisorB, getSelf(), 5000L);
        } else if (sender == supervisorB) {
            message.setReceiver(supervisorA);
            message.setSender(getSelf());

            message.messageScheduler(getContext(),message, supervisorA, getSelf(), 5000L);
        }else{
            System.out.println("Sender not defined at " + getSelf().path().name());
        }
    }
}
