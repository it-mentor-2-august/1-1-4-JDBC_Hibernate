package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import static java.lang.System.*;
import static java.lang.System.err;

public class Util {
    private static Connection connection = null;
    private static SessionFactory sessionFactory;
    private static final Properties properties;

    static {
        try {
            Class.forName("org.postgresql.Driver");
            out.println("PostgreSql драйвер зарегистрирован");
        } catch (ClassNotFoundException e) {
            err.println("Ошибка: Драйвер org.postgresql.Driver не найден");

        }
    }

    static {
        properties = new Properties();
        try(InputStream fileInputStream = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            err.println("Ошибка чтения файла db.resources: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }


    public static Connection getConnection() {
        try {
            String url = properties.getProperty("url");
            String username = properties.getProperty("user");
            String password = properties.getProperty("password");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            err.println("SQLException: " + e.getMessage()
                    + "SQLState: " + e.getSQLState());
            throw new DatabaseException("Ощибка соединения с БД", e);
        }

        return connection;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null){
            sessionFactory = new Configuration()
                    .addAnnotatedClass(User.class)
                    .setProperty("hibernate.connection.driver_class", properties.getProperty("driver"))
                    .setProperty("hibernate.connection.url", properties.getProperty("url"))
                    .setProperty("hibernate.connection.username", properties.getProperty("user"))
                    .setProperty("hibernate.connection.password", properties.getProperty("password"))
                    .setProperty("hibernate.dialect", properties.getProperty("dialect"))
                    .setProperty("hibernate.show_sql", "true")
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void closeSessionFactory() {
        sessionFactory.close();
    }
}

class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
