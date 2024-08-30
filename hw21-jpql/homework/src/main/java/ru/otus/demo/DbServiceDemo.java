package ru.otus.demo;

import java.util.List;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.crm.service.DbServiceClientImpl;

@SuppressWarnings({"java:S125", "java:S1481", "java:S1854"})
public class DbServiceDemo {

    private static final Logger log = LoggerFactory.getLogger(DbServiceDemo.class);

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

        // !!! временно - для вывода логов создания таблиц Hibernate
        // -------------------------------------
        //        try (var sessionFactory = HibernateUtils.buildSessionFactory(Client.class, Address.class,
        // Phone.class)) {
        //            log.info("Statistics:{}", sessionFactory.getStatistics());
        //            log.info("Hibernate version: {}", org.hibernate.Version.getVersionString());
        //        }
        // -------------------------------------

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
        dbServiceClient.findAll().forEach(client -> log.info("client:{}", client));
    }
}