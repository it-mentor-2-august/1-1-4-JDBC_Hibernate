package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.util.List;

import static java.lang.System.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try(UserService userService = new UserServiceImpl()){
            // Создание таблицы пользователей
            userService.createUsersTable();
            // Добавление нового пользователя
            userService.saveUser("John", "Dow", (byte) 30);
            userService.saveUser("Jane", "Dowe", (byte) 25);

            // Получение списка всех пользователей
            List<User> userList = userService.getAllUsers();
            if (userList != null && !userList.isEmpty()) userList.forEach(out::println);
            // Очистка таблицы пользователей users
            userService.cleanUsersTable();
            // Удаление таблицы пользователей
            userService.dropUsersTable();
        }
    }
}
