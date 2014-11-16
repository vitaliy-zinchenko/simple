package redis.task;

import com.google.common.collect.testing.MapInterfaceTest;
import org.apache.commons.collections.map.HashedMap;
import org.junit.After;
import org.junit.Before;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

import java.util.Map;

/**
 * Created by zinchenko on 11.11.14.
 */
public class MapTest extends MapInterfaceTest<String, String> {

    protected JedisPool jedisPool;

    protected RedisServer redisServer;


    public MapTest() {
        super(true, true, true,
                true, true);
    }

    protected void setUp() throws java.lang.Exception {
        redisServer = new RedisServer(6380);
        redisServer.start();

        jedisPool = new JedisPool("localhost", 6380);

    }

    protected void tearDown() throws java.lang.Exception {
        redisServer.stop();
        jedisPool.destroy();
    }

    @Override
    protected Map<String, String> makeEmptyMap() throws UnsupportedOperationException {
        return new RemoteMap<String, String>(jedisPool, "testMap", new BinarySerializer());
    }

    @Override
    protected Map<String, String> makePopulatedMap() throws UnsupportedOperationException {
        Map<String, String> map = new RemoteMap<String, String>(jedisPool, "testMap", new BinarySerializer());
        map.put("k", "v");
        return map;
    }

    @Override
    protected String getKeyNotInPopulatedMap() throws UnsupportedOperationException {
        return "kk";
    }

    @Override
    protected String getValueNotInPopulatedMap() throws UnsupportedOperationException {
        return "vv";
    }
}
