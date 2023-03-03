package org.example;

import akka.actor.*;

public class ActorOne extends AbstractActor {
    @Override
    public void preStart() throws Exception {
        super.preStart();
        Message message = new Message(getContext().self(),ActorRef.noSender());
        System.out.println("Message has been created");

        onMessage(message);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Message.class, this::onMessage)
                .build();
    }

    private void onMessage(Message message) {

        System.out.println();

        try {
            ActorRef supervisorA = getContext().getParent();

            message.setReceiver(supervisorA);
            message.setSender(getSelf());

            message.messageScheduler(getContext(),message,supervisorA,getSelf(),5000L);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
