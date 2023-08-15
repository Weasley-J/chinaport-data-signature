package cn.alphahub.eport.signature.interview;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently Unused
 *
 * @author weasley
 * @version 1.0
 * @date 2022/5/24
 */
public class LRUCache2<K, V> {
    /**
     * 坑位
     */
    private final int cacheSize;
    /**
     * 查找
     */
    private final Map<K, Node<K, V>> map;
    /**
     * 双向链表
     */
    private final DoubleLinkedList<K, V> doubleLinkedList;

    public LRUCache2(int cacheSize) {
        this.cacheSize = cacheSize;
        this.map = new HashMap<>();
        this.doubleLinkedList = new DoubleLinkedList<>();
    }

    public int getCacheSize() {
        return cacheSize;
    }

    public Map<K, Node<K, V>> getMap() {
        return map;
    }

    public DoubleLinkedList<K, V> getDoubleLinkedList() {
        return doubleLinkedList;
    }

    public V get(int key) {
        if (!map.containsKey(key)) {
            return null;
        }
        Node<K, V> node = map.get(key);
        doubleLinkedList.remove(node);
        doubleLinkedList.add(node);
        return node.value;
    }

    public void put(K key, V value) {
        if (map.containsKey(key)) {
            Node<K, V> node = map.get(key);
            node.value = value;
            map.put(key, node);

            doubleLinkedList.remove(node);
            doubleLinkedList.add(node);
        } else {
            if (map.size() == cacheSize) {
                Node<K, V> last = doubleLinkedList.getLast();
                map.remove(last.key);
                doubleLinkedList.remove(last);
            }
            Node<K, V> node = new Node<>(key, value);
            map.put(key, node);
            doubleLinkedList.add(node);
        }
    }

    /**
     * 构建一个Node数据结构
     */
    public static class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> prev;
        private Node<K, V> next;

        public Node() {
            this.prev = null;
            this.next = null;
        }

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
            this.prev = null;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public Node<K, V> getPrev() {
            return prev;
        }

        public Node<K, V> getNext() {
            return next;
        }
    }

    /**
     * 双向链表
     */
    public static class DoubleLinkedList<K, V> {
        private final Node<K, V> head;
        private final Node<K, V> tail;

        public DoubleLinkedList() {
            head = new Node<>();
            tail = new Node<>();
            //首尾相接: head.next -> tail -> tail.prev -> head
            head.next = tail;
            tail.prev = head;
        }

        public Node<K, V> getHead() {
            return head;
        }

        public Node<K, V> getTail() {
            return tail;
        }

        /**
         * 添加头节点
         */
        public void add(Node<K, V> node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }

        /**
         * 删除节点
         */
        public void remove(Node<K, V> node) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
            node.prev = null;
            node.next = null;
        }

        /**
         * 获取尾节点
         */
        public Node<K, V> getLast() {
            return tail.prev;
        }
    }
}
