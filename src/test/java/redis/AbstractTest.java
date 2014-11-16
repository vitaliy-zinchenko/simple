package redis;

import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

/**
 * Created by zinchenko on 09.11.14.
 */
public class AbstractTest {

    protected JedisPool jedisPool;

    protected RedisServer redisServer;

    @Before
    public void before() throws Exception {
//        redisServer = new RedisServer(6380);
//        redisServer.start();

        jedisPool = new JedisPool("localhost", 6379);
    }

    @After
    public void after() throws Exception {
//        redisServer.stop();

        jedisPool.destroy();
    }

    Jedis getJedis() {
        return jedisPool.getResource();
    }

    protected void p(Object s) {
        System.out.println(s);
    }

}
