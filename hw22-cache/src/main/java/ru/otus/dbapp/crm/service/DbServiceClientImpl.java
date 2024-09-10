package ru.otus.dbapp.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.dbapp.core.repository.DataTemplate;
import ru.otus.dbapp.core.sessionmanager.TransactionManager;
import ru.otus.dbapp.crm.model.Client;

import java.util.List;
import java.util.Optional;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;
    private HwCache<Long, Client> cache;

    public DbServiceClientImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;
        cacheInit();
    }

    @Override
    public Client saveClient(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();
            if (client.getId() == null) {
                var savedClient = clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                putInCache(savedClient);

                return savedClient;
            }
            var savedClient = clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", savedClient);
            putInCache(savedClient);

            return savedClient;
        });
    }

    @Override
    public Optional<Client> getClient(long id) {
        return getFromCache(id).or(() -> getFromDB(id));
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            clientList.forEach(this::putInCache);

            return clientList;
        });
    }

    private void cacheInit() {
        HwListener<Long, Client> listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };

        this.cache = new MyCache<>();
        this.cache.addListener(listener);
    }

    private void putInCache(Client client) {
        cache.put(client.getId(), client.clone());
    }

    private Optional<Client> getFromCache(long id) {
        return Optional.ofNullable(cache.get(id));
    }

    private Optional<Client> getFromDB(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("client: {}", clientOptional);
            clientOptional.ifPresent(this::putInCache);

            return clientOptional;
        });
    }
}
