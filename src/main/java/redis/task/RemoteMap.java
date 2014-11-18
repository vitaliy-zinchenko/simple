package redis.task;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by zinchenko
 */
public class RemoteMap<K, V> implements Map<K, V> {

    protected JedisPool jedisPool;

    protected String mapName;

    protected Serializer serializer;

    public RemoteMap(JedisPool jedisPool, String mapName, Serializer serializer) {
        this.jedisPool = jedisPool;
        this.mapName = mapName;
        this.serializer = serializer;
    }

    protected Jedis getJedis() {
        return jedisPool.getResource();
    }

    protected byte[] getMapNameBytes() {
        return mapName.getBytes();
    }

    protected byte[] serialize(Object key) {
        return serializer.serialize(key);
    }

    protected byte[] serializeValue(V value) {
        return serializer.serialize(value);
    }

    protected K deserializeKey(byte[] bytes) {
        return (K) serializer.deserialize(bytes);
    }

    protected V deserializeValue(byte[] bytes) {
        return (V) serializer.deserialize(bytes);
    }

    @Override
    public int size() {
        return executeCommand(new Command<Integer>() {
            @Override
            public Integer execute(Jedis jedis) throws Exception {
                return jedis.hlen(mapName).intValue();
            }
        });
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(final Object key) {
        return executeCommand(new Command<Boolean>() {
            @Override
            public Boolean execute(Jedis jedis) throws Exception {
                return jedis.hexists(getMapNameBytes(), serializer.serialize(key));
            }
        });
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(final Object key) {
        return executeCommand(new Command<V>() {
            @Override
            public V execute(Jedis jedis) throws Exception {
                byte[] value = jedis.hget(getMapNameBytes(), serialize(key));
                return deserializeValue(value);
            }
        });
    }

    @Override
    public V put(final K key, final V value) {
        return executeCommand(new Command<V>() {
            @Override
            public V execute(Jedis jedis) throws Exception {
                byte[] keyBytes =  serialize(key);
                byte[] valueBytes =  serializer.serialize(value);

                byte[] previous = jedis.hget(getMapNameBytes(), keyBytes);

                jedis.hset(getMapNameBytes(), keyBytes, valueBytes);
                return previous == null ? null : (V) serializer.deserialize(previous);
            }
        });
    }

    @Override
    public V remove(final Object key) {
        return executeCommand(new Command<V>() {
            @Override
            public V execute(Jedis jedis) throws Exception {
                byte[] removed = jedis.hget(getMapNameBytes(), serializer.serialize(key));
                return (V) serializer.deserialize(removed);
            }
        });
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        executeCommand(new Command<Void>() {
            @Override
            public Void execute(Jedis jedis) throws Exception {
                Map<byte[], byte[]> map = new HashMap<>();
                for(Entry<? extends K, ? extends V> entry: m.entrySet()) {
                    byte[] key = serializer.serialize(entry.getKey());
                    byte[] value = serializer.serialize(entry.getValue());
                    map.put(key, value);
                }
                jedis.hmset(getMapNameBytes(), map);
                return null;
            }
        });
    }

    @Override
    public void clear() {
        executeCommand(new Command<Void>() {
            @Override
            public Void execute(Jedis jedis) throws Exception {
                jedis.del(mapName);
                return null;
            }
        });
    }

    @Override
    public Set<K> keySet() {
        return executeCommand(new Command<Set<K>>() {
            @Override
            public Set<K> execute(Jedis jedis) throws Exception {
                Set<byte[]> set = jedis.hkeys(getMapNameBytes());
                Set<K> result = new HashSet<K>();
                for(byte[] item: set) {
                    K k = (K) serializer.deserialize(item);
                    result.add(k);
                }
                return result;
            }
        });
    }

    @Override
    public Collection<V> values() {
        return executeCommand(new Command<Collection<V>>() {
            @Override
            public Collection<V> execute(Jedis jedis) throws Exception {
                List<byte[]> values = jedis.hvals(getMapNameBytes());
                List result = new ArrayList<>();
                for(byte[] item: values) {
                    result.add(serializer.deserialize(item));
                }
                return result;
            }
        });
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return executeCommand(new Command<Set<Entry<K, V>>>() {
            @Override
            public Set<Entry<K, V>> execute(Jedis jedis) throws Exception {
                Map<byte[], byte[]> map = jedis.hgetAll(getMapNameBytes());
                Set<Entry<K, V>> result = new HashSet<Entry<K, V>>();
                for(Entry<byte[], byte[]> mapEntry: map.entrySet()) {
                    K k = (K) serializer.deserialize(mapEntry.getKey());
                    V v = (V) serializer.deserialize(mapEntry.getValue());
                    Entry entry = new AbstractMap.SimpleEntry<K, V>(k, v);
                    result.add(entry);
                }
                return result;
            }
        });
    }

    @Override
    public int hashCode() {
        return 29;
    }

    protected <R> R executeCommand(Command<R> command) {
        try(Jedis jedis = getJedis()) {
            return command.execute(jedis);
        } catch (Exception e) {
            throw new UnexpectedRemoteException();
        }
    }

    protected interface Command<R> {
        R execute(Jedis jedis) throws Exception;
    }

}
