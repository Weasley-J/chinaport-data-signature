package cn.alphahub.eport.signature.interview;

import org.junit.jupiter.api.Test;

/**
 * Least Recently Unused
 *
 * @author weasley
 * @version 1.0
 * @date 2022/5/24
 */
public class LRUCacheTests {
    @Test
    void LRUCache1() {
        LRUCache1<Integer, Object> cache = new LRUCache1<>(3);
        cache.put(1, "a");
        cache.put(2, "a");
        cache.put(3, "a");
        System.out.println(cache.keySet());
        cache.put(4, "q");
        System.out.println(cache.keySet());
    }

    @Test
    void LRUCache2() {
        LRUCache2<Integer,Integer> lruCache = new LRUCache2<>(3);
        lruCache.put(1, 1);
        lruCache.put(2, 1);
        lruCache.put(3, 1);
        System.out.println(lruCache.getMap().keySet());
        lruCache.put(4, 1);
        System.out.println(lruCache.getMap().keySet());
        lruCache.put(3, 1);
        System.out.println(lruCache.getMap().keySet());
    }
}
