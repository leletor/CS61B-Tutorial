package lab9;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  @author Your name here
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    private static final int DEFAULT_SIZE = 16;
    private static final double MAX_LF = 0.75;

    private ArrayMap<K, V>[] buckets;
    private int size;

    private int loadFactor() {
        return size / buckets.length;
    }

    public MyHashMap() {
        buckets = new ArrayMap[DEFAULT_SIZE];
        this.clear();
    }
    private MyHashMap(int capacity){
        buckets = new ArrayMap[capacity];
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        this.size = 0;
        for (int i = 0; i < this.buckets.length; i += 1) {
            this.buckets[i] = new ArrayMap<>();
        }
    }

    /** Computes the hash function of the given key. Consists of
     *  computing the hashcode, followed by modding by the number of buckets.
     *  To handle negative numbers properly, uses floorMod instead of %.
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }

        int numBuckets = buckets.length;
        return Math.floorMod(key.hashCode(), numBuckets);
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        return buckets[hash(key)].get(key);
    }

    /* Associates the specified value with the specified key in this map. */
    @Override
    public void put(K key, V value) {
        if (key == null) return;
        if (loadFactor() > MAX_LF){
            resizeTwice();
        }
        if (buckets[hash(key)].containsKey(key)){
            buckets[hash(key)].remove(key);
            buckets[hash(key)].put(key,value);
        }
        else {
            buckets[hash(key)].put(key, value);
            size += 1;
        }
    }
    private void resizeTwice(){
        MyHashMap<K,V>  m= new MyHashMap<>(size*2);
        for (int i = 0;i<buckets.length;i++){
            for (K k :buckets[i]){
                m.put(k,get(k));
            }
        }
        this.buckets = m.buckets;
        this.size = m.size;
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
    }

    /* Removes the mapping for the specified key from this map if exists.
     * Not required for this lab. If you don't implement this, throw an
     * UnsupportedOperationException. */
    @Override
    public V remove(K key) {
        if (!buckets[hash(key)].containsKey(key))
            return null;
        V value = buckets[hash(key)].get(key);
        buckets[hash(key)].remove(key);
        size -=1;
        return value;
    }

    /* Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for this lab. If you don't implement this,
     * throw an UnsupportedOperationException.*/
    @Override
    public V remove(K key, V value) {
        if (!buckets[hash(key)].containsKey(key)||!buckets[hash(key)].get(key).equals(value))
            return null;
        buckets[hash(key)].remove(key);
        size -=1;
        return value;
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException();
    }



}
