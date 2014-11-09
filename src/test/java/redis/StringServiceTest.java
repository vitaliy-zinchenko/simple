package redis;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by zinchenko on 09.11.14.
 */
public class StringServiceTest {

    JedisPool jedisPool;

    RedisServer redisServer;

    StringService stringService;

    @Before
    public void before() throws Exception {
        redisServer = new RedisServer(6379);
        redisServer.start();

        jedisPool = new JedisPool("localhost", 6379);
    }

    @After
    public void after() throws Exception {
        redisServer.stop();

        jedisPool.destroy();
    }

    Jedis getJedis() {
        return jedisPool.getResource();
    }

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
    public void test() {
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
