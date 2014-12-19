package akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

/**
 * Created by zinchenko on 17.12.14.
 */
public class MySystem {
    public static void main(String[] args) throws InterruptedException {
        final ActorSystem actorSystem = ActorSystem.create("actorSystem");

        Thread.sleep(5000);

        final ActorRef actorRef = actorSystem.actorOf(Props.create(MyActor.class), "simpleActor");

        actorRef.tell(new Command("CMD 1"), null);
        actorRef.tell(new Command("CMD 2"), null);
        actorRef.tell(new Command("CMD 3"), null);
        actorRef.tell(new Command("CMD 4"), null);
        actorRef.tell(new Command("CMD 5"), null);

        Thread.sleep(5000);

        System.out.println("Actor System Shutdown Starting...");

        actorSystem.shutdown();
    }
}
