package redis.task;

import org.apache.commons.lang.SerializationUtils;

import java.io.Serializable;

/**
 * Created by zinchenko on 11.11.14.
 */
public class BinarySerializer implements Serializer {

    @Override
    public byte[] serialize(Object o) {
        return SerializationUtils.serialize((Serializable)o);
    }

    @Override
    public Object deserialize(byte[] bytes) {
        return SerializationUtils.deserialize(bytes);
    }
}
