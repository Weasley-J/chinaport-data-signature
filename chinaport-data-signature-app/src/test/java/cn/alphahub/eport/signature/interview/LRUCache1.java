package cn.alphahub.eport.signature.interview;

import java.util.LinkedHashMap;
import java.util.Map;

public class LRUCache1<K, V> extends LinkedHashMap<K, V> {
    private final int initialCapacity;

    public LRUCache1(int initialCapacity) {
        super(initialCapacity, 0.75F, false);
        this.initialCapacity = initialCapacity;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return super.size() > this.initialCapacity;
    }
}
