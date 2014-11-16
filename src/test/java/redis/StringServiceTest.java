package redis;

import com.google.common.base.Charsets;
import org.apache.commons.lang.SerializationUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by zinchenko on 09.11.14.
 */
public class StringServiceTest extends AbstractTest {

    @Test
    public void testSetGet() {
        try (Jedis jedis = getJedis()) {
            String answer = jedis.set("my-key", "my-value");
            System.out.println(answer);
        }

        try (Jedis jedis = getJedis()) {
            assertEquals("my-value", jedis.get("my-key"));
        }
    }

    @Test
    public void testIncr() {
        try (Jedis jedis = getJedis()) {
            String s = jedis.get("k");
            assertNull(s);
        }

        try (Jedis jedis = getJedis()) {
            jedis.set("k", "10");
            assertEquals("10", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.incr("k");
            assertEquals("11", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.incrBy("k", 2);
            assertEquals("13", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.decr("k");
            assertEquals("12", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.decrBy("k", 2);
            assertEquals("10", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.incrByFloat("k", 1.5);
            assertEquals("11.5", jedis.get("k"));
        }

        try (Jedis jedis = getJedis()) {
            jedis.incrByFloat("k", -1.5);
            assertEquals("10", jedis.get("k"));
        }
    }

    @Test
    public void testAppend() {
        try(Jedis jedis = getJedis()) {
            jedis.set("k", "first ");
            jedis.append("k", "second");
            assertEquals("first second", jedis.get("k"));
        }
        try(Jedis jedis = getJedis()) {
            assertEquals("rst", jedis.getrange("k", 2, 4));
        }
        try(Jedis jedis = getJedis()) {
            jedis.setrange("k", 5, "!");
            assertEquals("first!second", jedis.get("k"));
        }
    }

    @Test
    public void testByte() throws Exception {
        {
            byte[] b = {65}; //  [01000001]
            String s = new String(b);
            p(s);
        }

        {
            p("A".getBytes(Charsets.UTF_8)[0]);

            byte[] key = "kb".getBytes(Charsets.UTF_8);
            byte[] value = "value".getBytes(Charsets.UTF_8);
            try(Jedis jedis = getJedis()) {
                jedis.set(key, value);
                byte[] response = jedis.get(key);
                p("response length = " + response.length);
                for (int i = 0; i < response.length; i++) {
                    p(response[i]);
                }
            }
        }

        {
            TestBean testBean = new TestBean();
            testBean.name = "name";
            testBean.age = 15;

            byte[] key = "key".getBytes();
            byte[] value = SerializationUtils.serialize(testBean);

            try(Jedis jedis = getJedis()) {
                jedis.set(key, value);
                byte[] response = jedis.get(key);
                TestBean resposeBean = (TestBean) SerializationUtils.deserialize(response);
                assertEquals(testBean.name, resposeBean.name);
                assertEquals(testBean.age, resposeBean.age);
            }
        }

        try(Jedis jedis = getJedis()) {
            jedis.set("k", "A");

            assertEquals(false, jedis.getbit("k", 0));
            assertEquals(true, jedis.getbit("k", 1));
            assertEquals(false, jedis.getbit("k", 2));
            assertEquals(false, jedis.getbit("k", 3));
            assertEquals(false, jedis.getbit("k", 4));
            assertEquals(false, jedis.getbit("k", 5));
            assertEquals(false, jedis.getbit("k", 6));
            assertEquals(true, jedis.getbit("k", 7));
        }
    }

    @Test
    public void fill() {
        try(Jedis jedis = getJedis()) {
            for (int i = 0; i < 1000000; i++) {
                String s = "start";
                for (int j = 0; j < 1000; j++) {
                    s += " qwwwwwwwwwwwwwwwwwwwww ssssssssss sssssssssdddddddddddddd dffffffffffffg  ggggggggg";
                }
                String k = "key-"+System.currentTimeMillis();
                jedis.set(k, s);
//                System.out.println(i);
//                System.out.println(k);
//                System.out.println(s);
            }
        }
    }


//    @Test
//    public void test() {
//
//    }
//
//    @Test
//    public void test() {
//
//    }
//
//    @Test
//    public void test() {
//
//    }

}
