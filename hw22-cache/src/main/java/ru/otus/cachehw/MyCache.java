package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private static final Logger logger = LoggerFactory.getLogger(MyCache.class);

    private final Map<K, V> cache;
    private final List<HwListener<K, V>> listeners;

    public MyCache() {
        this.cache = new WeakHashMap<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
        listeners.forEach(listener -> listener.notify(key, value, "updated"));
        logger.info("Cache size after addition: {}", cache.size());
    }

    @Override
    public void remove(K key) {
        V removed = cache.remove(key);
        listeners.forEach(listener -> listener.notify(key, removed, "removed"));
        logger.info("Cache size after removing: {}", cache.size());
    }

    @Override
    public V get(K key) {
        V gotten = cache.get(key);
        listeners.forEach(listener -> listener.notify(key, gotten, "get"));
        logger.info("Getting from cache: {}", gotten.toString());

        return gotten;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(listener);
        logger.info("Listener added: {}", listener.getClass().getName());
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.remove(listener);
        logger.info("Listener removed: {}", listener.getClass().getName());
    }
}
