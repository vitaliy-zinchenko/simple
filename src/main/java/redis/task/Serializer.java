package redis.task;

/**
 * Created by zinchenko on 09.11.14.
 */
public interface Serializer {

    byte[] serialize(Object o);
    Object deserialize(byte[] bytes);


}
