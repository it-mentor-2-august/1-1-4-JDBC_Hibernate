package jm.task.core.jdbc.factory;

import jm.task.core.jdbc.dao.UserDao;
import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;

public class UserDaoFactory {
    public UserDao getByProperty() {
        if (getProperty().equals("jdbc")) {
            return new UserDaoJDBCImpl();
        } else if(getProperty().equals("jpa")){
            return new UserDaoHibernateImpl(Util.getSessionFactory());
        }
        else {
            throw new RuntimeException("UserDao не найден");
        }
    }

    public String getProperty(){
        Properties properties = new Properties();
        try (InputStream fileInputStream = Util.class.getClassLoader().getResourceAsStream("db.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            System.err.println("Ошибка доступа к файлу db.properties");
            e.printStackTrace();
        }
        return properties.getProperty("daotype");
    }
}
