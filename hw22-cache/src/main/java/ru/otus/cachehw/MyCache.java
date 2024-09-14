package ru.otus.cachehw;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.exception.NotifyException;

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
        V oldValue = cache.put(key, value);
        notifyListeners(key, value, "updated");
        if (oldValue == null) {
            logger.info("Cache size after addition: {}", cache.size());
        } else {
            logger.info("Cache size after updated: {}", cache.size());
        }
    }

    @Override
    public void remove(K key) {
        V removed = cache.remove(key);
        notifyListeners(key, removed, "removed");
        logger.info("Cache size after removing: {}", cache.size());
    }

    @Override
    public V get(K key) {
        V gotten = cache.get(key);
        notifyListeners(key, gotten, "get");
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

    private void notifyListeners(K key, V value, String action) {
        try {
            listeners.forEach(listener -> listener.notify(key, value, action));
        } catch (Exception e) {
            throw new NotifyException(e.getMessage(), e);
        }
    }
}
