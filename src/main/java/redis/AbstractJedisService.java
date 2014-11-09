package redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zinchenko on 09.11.14.
 */
public abstract class AbstractJedisService {

    protected JedisPool jedisPool;

    public AbstractJedisService() {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
    }

    public void destroy() {
        jedisPool.destroy();
    }

    protected Jedis getJedis() {
        return jedisPool.getResource();
    }

}
