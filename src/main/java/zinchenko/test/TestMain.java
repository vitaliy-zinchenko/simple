package zinchenko.test;

import com.hazelcast.config.Config;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.impl.Transaction;
import zinchenko.Bean;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zinchenko on 24.07.14.
 */
public class TestMain {
    public static void main(String[] args) throws InterruptedException {
        Config cfg = new Config();
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
        List<Member> mapMembers = instance.getList("members");

        mapMembers.add(new Member(1L, "m1"));
        mapMembers.add(new Member(2L, "m2"));
        mapMembers.add(new Member(3L, "m3"));

        while (true) {
            Collection<Member> objects = instance.getList("members");
            for (Member object : objects) {
                System.out.println(object.toString());
            }
            System.out.println("sleep");
            Thread.sleep(2000L);
        }
    }
}
