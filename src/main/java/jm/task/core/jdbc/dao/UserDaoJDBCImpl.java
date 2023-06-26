package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() throws SQLException, ClassNotFoundException {

    }


    public void createUsersTable() {

        String sql = "CREATE TABLE IF NOT EXISTS usertable (id INT NOT NULL AUTO_INCREMENT, name VARCHAR(45) NOT NULL, lastName VARCHAR(45) NOT NULL, age INT NOT NULL, PRIMARY KEY (id))";
        try (Connection connection = Util.getConnection(); Statement createUT = connection.createStatement()) {

            createUT.executeUpdate(sql);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {

        String sql = "drop table if exists usertable";
        try (Connection connection = Util.getConnection(); Statement dropUT = connection.createStatement();) {
            dropUT.executeUpdate(sql);
            System.out.println("Таблица удалена");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {

        String sql = "insert usertable(name, lastName, age) values (?, ?, ?)";
        try (Connection connection = Util.getConnection(); PreparedStatement saveUser = connection.prepareStatement(sql)) {

            try {
                connection.setAutoCommit(false);
                saveUser.setString(1, name);
                saveUser.setString(2, lastName);
                saveUser.setByte(3, age);
                saveUser.executeUpdate();
                connection.commit();
                System.out.println(String.format("User с именем – %s добавлен в базу данных ", name));
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException();
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void removeUserById(long id) {
        String sql = "delete from usertable where id = ?";
        try (Connection connection = Util.getConnection(); PreparedStatement removeUser = connection.prepareStatement(sql)) {
            try {
                connection.setAutoCommit(false);
                removeUser.setLong(1, id);
                removeUser.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        String sql = "select * from usertable";

        try (Connection connection = Util.getConnection(); Statement getAllUsers = connection.createStatement();) {

            try {
                connection.setAutoCommit(false);
                ResultSet resultSet = getAllUsers.executeQuery(sql);
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLastName(resultSet.getString("lastName"));
                    user.setAge(resultSet.getByte("age"));
                    allUsers.add(user);
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return allUsers;
    }

    public void cleanUsersTable() {
        String sql = "delete from usertable";
        try (Connection connection = Util.getConnection(); PreparedStatement cleanUT = connection.prepareStatement(sql)) {
            try {
                connection.setAutoCommit(false);
                cleanUT.executeUpdate();
                System.out.println("Таблица очищена");
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
