package redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zinchenko on 09.11.14.
 */
public class StringService extends AbstractJedisService {

    public String get(String key) {
        return getJedis().get(key);
    }

    public String set(String key, String value) {
        return getJedis().set(key, value);
    }

    public void incr() {
        return;
    }

}
