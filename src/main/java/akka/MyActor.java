package akka;

import akka.actor.UntypedActor;

import java.util.UUID;

/**
 * Created by zinchenko on 17.12.14.
 */
public class MyActor extends UntypedActor {

    public MyActor() {
        System.out.println("SimpleActor constructor");
    }

    @Override
    public void onReceive(Object msg) throws Exception {

        System.out.println("Received Command: " + msg + " Thread is " + Thread.currentThread().getName());

        if (msg instanceof Command) {
            final String data = ((Command) msg).getData();
            final Event event = new Event(data, UUID.randomUUID().toString());

            // emmit an event somewhere...

        } else if (msg.equals("echo")) {
            System.out.println("ECHO!");
        }
    }
}
