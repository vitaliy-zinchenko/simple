package redis;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Assert;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import static org.junit.Assert.assertEquals;

/**
 * Created by zinchenko on 09.11.14.
 */
public class HashTest extends AbstractTest {

    private String k = "hk";

    @Test
    public void test() {
        try(Jedis jedis = getJedis()) {
            jedis.hset(k, "f1", "v1");
            assertEquals("v1", jedis.hget(k, "f1"));
        }

        {
            TestBean testBean = new TestBean();
            testBean.name = "n";
            testBean.age = 10;

            byte[] key = "fb1".getBytes();
            byte[] value = SerializationUtils.serialize(testBean);
            try(Jedis jedis = getJedis()) {
                jedis.hset(k.getBytes(), key, value);
                byte[] result = jedis.hget(k.getBytes(), key);
                TestBean resultBean = (TestBean) SerializationUtils.deserialize(result);
                assertEquals("n", resultBean.name);
                assertEquals(10, resultBean.age);
            }

        }
    }






}
