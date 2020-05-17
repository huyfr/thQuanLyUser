package dao;

import common.Error;
import model.User;
import common.Jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO implements IUserDAO {

    public UserDAO() {
    }

    protected Connection getConnection() {
        Connection connection = Jdbc.CONNECTION_NULL;
        try {
            Class.forName(Jdbc.JDBC_DRIVER);
            connection = DriverManager.getConnection(Jdbc.DATABASE_URL, Jdbc.USER, Jdbc.PASSWORD);
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_001);
        } catch (ClassNotFoundException ex) {
            System.err.println(Error.ERROR_002);
        }
        return connection;
    }

    @Override
    public void insertUser(User user) {
        Connection connection;
        PreparedStatement preparedStatement;
        System.out.println(Jdbc.INSERT_USERS_SQL);
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.INSERT_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_003);
        }
    }

    @Override
    public User selectUser(int id) {
        User user = null;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet rs;
        String name;
        String email;
        String country;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                name = rs.getString(Jdbc.COLUMN_NAME);
                email = rs.getString(Jdbc.COLUMN_EMAIL);
                country = rs.getString(Jdbc.COLUMN_COUNTRY);
                user = new User(id, name, email, country);
            }
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_005);
        }
        return user;
    }

    @Override
    public List<User> selectAllUsers() {
        List<User> users = null;
        Connection connection;
        PreparedStatement preparedStatement;
        ResultSet resultSet;
        int id;
        String name;
        String email;
        String country;

        try {
            users = new ArrayList<>();
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.SELECT_ALL_USERS);
            System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                id = resultSet.getInt(Jdbc.COLUMN_ID);
                name = resultSet.getString(Jdbc.COLUMN_NAME);
                email = resultSet.getString(Jdbc.COLUMN_EMAIL);
                country = resultSet.getString(Jdbc.COLUMN_COUNTRY);
                users.add(new User(id, name, email, country));
            }
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_004);
        }
        return users;
    }

    @Override
    public boolean deleteUser(int id) {
        boolean rowDeleted = false;
        Connection connection;
        PreparedStatement preparedStatement;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.DELETE_USERS_SQL);
            preparedStatement.setInt(1, id);
            rowDeleted = preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.err.println(Error.ERROR_006);
        }
        return rowDeleted;
    }

    @Override
    public boolean updateUser(User user) {
        boolean rowUpdated = false;
        Connection connection;
        PreparedStatement preparedStatement;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(Jdbc.UPDATE_USERS_SQL);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            preparedStatement.setInt(4, user.getId());
            rowUpdated = preparedStatement.executeUpdate() > 0;
        } catch (SQLException ex){
            System.err.println(Error.ERROR_007);
        }
        return rowUpdated;
    }
}