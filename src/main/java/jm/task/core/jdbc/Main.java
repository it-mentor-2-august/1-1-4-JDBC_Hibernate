package jm.task.core.jdbc;

import jm.task.core.jdbc.dao.UserDaoHibernateImpl;
import jm.task.core.jdbc.dao.UserDaoJDBCImpl;
import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserService userService = new UserServiceImpl();
        // Создание таблицы пользователей
        userService.createUsersTable();
        // Добавление нового пользователя
        userService.saveUser("John", "Dow", (byte) 30);
        userService.saveUser("Jane", "Dowe", (byte) 25);

        // Получение списка всех пользователей
        List<User> userList = userService.getAllUsers();
        userList.forEach(System.out::println);
        // Очистка таблицы пользователей users
         userService.cleanUsersTable();
        // Удаление таблицы пользователей
         userService.dropUsersTable();
    }
}
