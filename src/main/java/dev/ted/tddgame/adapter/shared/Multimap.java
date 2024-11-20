package dev.ted.tddgame.adapter.shared;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Multimap<K, V> {
    private final Map<K, Set<V>> map = new ConcurrentHashMap<>();

    public void put(K key, V value) {
        Set<V> values = map.computeIfAbsent(key, k -> new HashSet<>());
        values.add(value);
    }

    public Set<V> get(K key) {
        return map.getOrDefault(key, new HashSet<>());
    }

    public boolean remove(K key, V value) {
        Set<V> values = map.get(key);
        if (values != null) {
            boolean removed = values.remove(value);
            if (values.isEmpty()) {
                map.remove(key);
            }
            return removed;
        }
        return false;
    }
}
