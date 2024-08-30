package ru.otus.core.repository;

import java.util.Arrays;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public final class HibernateUtils {

    private HibernateUtils() {}

    public static SessionFactory buildSessionFactory(Configuration configuration, Class<?>... annotatedClasses) {
        MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
        Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    // временный метод для вывода логов создания таблиц Hibernate
    public static SessionFactory buildSessionFactory(Class<?>... annotatedClasses) {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create"); // !!! Только для упрощения учебного примера

        return buildSessionFactory(configuration, annotatedClasses);
    }

    private static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
    }
}
