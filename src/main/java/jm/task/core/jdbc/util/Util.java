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
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/example";
    private static final String JDBC_DRIVER = "org.postgresql.Driver";
    private static Connection connection = null;
    private static SessionFactory sessionFactory;

    static {
        try {
            Class.forName(JDBC_DRIVER);
            System.out.println("PostgreSql драйвер зарегистрирован");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSql драйвер не найден");
            throw new RuntimeException(e);
        }
    }


    public static Connection getConnection() throws SQLException {
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
                throw new RuntimeException(e);
            }

        }
        return connection;
    }

    public static Connection getConnection(String username, String password) throws SQLException {
       return DriverManager.getConnection(DB_URL, username, password);
    }

    public static SessionFactory getSessionFactory() {
        Properties properties = new Properties();
        try (InputStream fileInputStream = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
}
