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
        try(Jedis jedis = getJedis()) {
            return jedis.hlen(mapName).intValue();
        }
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        try(Jedis jedis = getJedis()) {
            return jedis.hexists(getMapNameBytes(), serializer.serialize(key));
        }
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        try(Jedis jedis = getJedis()) {
            byte[] value = jedis.hget(getMapNameBytes(), serialize(key));
            return deserializeValue(value);
        }
    }

    @Override
    public V put(K key, V value) {
        try(Jedis jedis = getJedis()) {
            byte[] keyBytes =  serialize(key);
            byte[] valueBytes =  serializer.serialize(value);

            byte[] previous = jedis.hget(getMapNameBytes(), keyBytes);

            jedis.hset(getMapNameBytes(), keyBytes, valueBytes);
            return previous == null ? null : (V) serializer.deserialize(previous);
        }
    }

    @Override
    public V remove(Object key) {
        try(Jedis jedis = getJedis()) {
            byte[] removed = jedis.hget(getMapNameBytes(), serializer.serialize(key));
            return (V) serializer.deserialize(removed);
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        try(Jedis jedis = getJedis()) {
            Map<byte[], byte[]> map = new HashMap<>();
            for(Entry<? extends K, ? extends V> entry: m.entrySet()) {
                byte[] key = serializer.serialize(entry.getKey());
                byte[] value = serializer.serialize(entry.getValue());
                map.put(key, value);
            }
            jedis.hmset(getMapNameBytes(), map);
        }
    }

    @Override
    public void clear() {
        try(Jedis jedis = getJedis()) {
            jedis.del(mapName);
        }
    }

    @Override
    public Set<K> keySet() {
        try(Jedis jedis = getJedis()) {
            Set<byte[]> set = jedis.hkeys(getMapNameBytes());
            Set<K> result = new HashSet<K>();
            for(byte[] item: set) {
                K k = (K) serializer.deserialize(item);
                result.add(k);
            }
            return result;
        }
    }

    @Override
    public Collection<V> values() {
        try(Jedis jedis = getJedis()) {
            List<byte[]> values = jedis.hvals(getMapNameBytes());
            List result = new ArrayList<>();
            for(byte[] item: values) {
                result.add(serializer.deserialize(item));
            }
            return result;
        }
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        try(Jedis jedis = getJedis()) {
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
    }

//    public static class RemoteEntry<K, V> implements Map.Entry<K, V> {
//
//        private K k;
//
//        private V v;
//
//        @Override
//        public K getKey() {
//            return k;
//        }
//
//        @Override
//        public V getValue() {
//            return v;
//        }
//
//        @Override
//        public V setValue(V value) {
//            V old = v;
//            v = value;
//            return old;
//        }
//
//        @Override
//        public int hashCode() {
//            return k.hashCode() + v.hashCode();
//        }
//
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Entry entry = (Entry) o;
//
//            if (k != null ? !k.equals(entry.k) : entry.k != null) return false;
//            if (v != null ? !v.equals(entry.v) : entry.v != null) return false;
//
//            return true;
//        }
//    }


    @Override
    public int hashCode() {
        return 29;
    }
}
