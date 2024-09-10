package ru.otus.dbapp.demo;

import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.dbapp.core.repository.DataTemplateHibernate;
import ru.otus.dbapp.core.repository.HibernateUtils;
import ru.otus.dbapp.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.dbapp.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.dbapp.crm.model.Address;
import ru.otus.dbapp.crm.model.Client;
import ru.otus.dbapp.crm.model.Phone;
import ru.otus.dbapp.crm.service.DbServiceClientImpl;

import java.util.List;

public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);
        var clientFirst = new Client(
                null, "Vasya", new Address(null, "VasyasStreet"), List.of(new Phone(null, "1"), new Phone(null, "2")));
        dbServiceClient.saveClient(clientFirst);

        var clientSecond = new Client(
                null, "Petya", new Address(null, "PetyasStreet"), List.of(new Phone(null, "3"), new Phone(null, "4")));
        var clientSecondSaved = dbServiceClient.saveClient(clientSecond);
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecondSaved.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSaved.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);
        ///
        dbServiceClient.saveClient(new Client(clientSecondSelected.getId(), "PetyaUpdated"));
        var clientUpdated = dbServiceClient
                .getClient(clientSecondSelected.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecondSelected.getId()));
        log.info("clientUpdated:{}", clientUpdated);

        log.info("All clients");
        dbServiceClient
                .findAll()
                .forEach(client -> log.info(
                        "client: {}, address: {}, phones: {}", client, client.getAddress(), client.getPhones()));
    }
}
