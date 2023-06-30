package jm.task.core.jdbc;

import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {
    public static void main(String[] args) {
        UserServiceImpl userService = new UserServiceImpl();
        userService.createUsersTable();
        userService.saveUser("Sergei", "Popov", (byte) 23);
        userService.saveUser("Alex", "Ivanov", (byte) 19);
        userService.saveUser("Maxim", "Petrov", (byte) 29);
        userService.saveUser("Mikhail", "Sidorov", (byte) 34);
       /* System.out.println(userService.getAllUsers());
        userService.cleanUsersTable();
        userService.dropUsersTable();*/

    }
}
