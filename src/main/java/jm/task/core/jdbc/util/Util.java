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

public class Util {
    private static Connection connection = null;
    private static SessionFactory sessionFactory;

    static {
        try {
            Class<?> aClass = Class.forName("org.postgresql.Driver");
            System.out.println("PostgreSql драйвер зарегистрирован");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSql драйвер не найден");
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() {
        if (connection != null) {
            return connection;
        } else {
            try {
                Properties properties = new Properties();
                try (InputStream fileInputStream = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
                    properties.load(fileInputStream);
                }
                String url = properties.getProperty("url");
                String username = properties.getProperty("user");
                String password = properties.getProperty("password");
                connection = DriverManager.getConnection(url, username, password);
            } catch (IOException e) {
                throw new DatabaseException("Ошибка в чтении файла конфигурации", e);
            } catch (SQLException e) {
                throw new DatabaseException("Ощибка соединения с БД", e);
            }

        }
        return connection;
    }

    public static SessionFactory getSessionFactory() {
        Properties properties = new Properties();
        try (InputStream fileInputStream = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new DatabaseException("Ошибка в чтении файла конфигурации", e);
        }
        if (sessionFactory == null) {
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

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseException("Соединение с БД закрылось с ошибкой", e);
        }
    }
}

class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
