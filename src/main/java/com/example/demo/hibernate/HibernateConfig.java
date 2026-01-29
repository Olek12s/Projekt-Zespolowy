package com.example.demo.hibernate;
import com.example.demo.model.Friends;
import com.example.demo.model.MatchHistory;
import com.example.demo.model.User;
import io.github.cdimascio.dotenv.Dotenv;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

public class HibernateConfig {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {

                Dotenv dotenv = Dotenv.load();
                Configuration configuration = new Configuration();

                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "org.postgresql.Driver");
                settings.put(Environment.URL, dotenv.get("DATABASE_URL"));
                settings.put(Environment.USER, dotenv.get("DB_USER"));
                settings.put(Environment.PASS, dotenv.get("DB_PASSWORD"));
                settings.put(Environment.DIALECT, "org.hibernate.dialect.PostgreSQLDialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(MatchHistory.class);
                configuration.addAnnotatedClass(Friends.class);

                sessionFactory = configuration.buildSessionFactory();

            } catch (Throwable ex) {
                throw new ExceptionInInitializerError("Inicialize Hibernate ERROR: " + ex);
            }
        }
        return sessionFactory;
    }
}