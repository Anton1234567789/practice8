package ua.nure.sokolov.practice8;

import ua.nure.sokolov.practice8.entity.Group;
import ua.nure.sokolov.practice8.entity.User;
import ua.nure.sokolov.practice8.entity.UserGroup;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBManager {


    private static final String SQL_INSERT_USER = "INSERT INTO users (login) VALUES (?);";
    private static final String SQL_GET_ALL_USERS = "SELECT * FROM users";
    private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users u WHERE u.login = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    private static final String SQL_UPDATE_USER = "UPDATE users SET login = ? WHERE user_id = ?";

    private static final String SQL_GET_ALL_GROUP = "SELECT * FROM groups";
    private static final String SQL_INSERT_GROUP = "INSERT INTO groups (name) VALUES (?);";
    private static final String SQL_FIND_GROUP_BY_NAME = "SELECT * FROM groups g WHERE g.name = ?";
    private static final String SQL_GET_USER_GROUP =
            "SELECT * FROM groups g, users_groups ug WHERE g.group_id = ug.group_id";
    private static final String SQL_INSERT_USER_GROUP = "INSERT INTO users_groups VALUES " +
            "((SELECT user_id FROM users u WHERE u.login = ?) ,(SELECT group_id FROM groups g WHERE g.name = ?))";


    private static final String CONNECTION_URL = "jdbc:mysql://localhost:3306/practice8" +
            "?user=testuser&password=testuser&useSSL=false";


    private static DBManager instance;

    public static synchronized DBManager getInstance(){
        if (instance==null){
            instance = new DBManager();
        }
        return instance;
    }

    public DBManager() {
    }



    public List<User> findAllUsers() throws SQLException, DBException {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(SQL_GET_ALL_USERS);
            while (rs.next()){
                User user = extractUser(rs);
                users.add(user);
            }
            return users;
        }catch (SQLException ex){
            throw new DBException("Cannot find all users with table users", ex);
        }finally {
            close(connection);
            close(statement);
            close(rs);
        }
    }

    private User extractUser(ResultSet rs) throws SQLException {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setLogin(rs.getString("login"));

        return user;

    }

    private Group extractGroup(ResultSet rs)throws SQLException{
        Group group = new Group();

        group.setGroupId(rs.getInt("group_id"));
        group.setName(rs.getString("name"));

        return group;
    }

    public boolean insertUser(User user) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_USER, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            preparedStatement.setString(k++, user.getLogin());

            if (preparedStatement.executeUpdate() > 0){
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    user.setUserId(rs.getInt(1));
                    res = true;
                }
            }

        } catch (SQLException e) {
            throw new DBException("Cannot insert user: " +  user, e);
        }finally {
            close(connection);
            close(preparedStatement);
            close(rs);
        }

        return res;
    }

    public List<Group> findAllGroups() throws SQLException, DBException {
        List<Group> groups = new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();

            statement = connection.createStatement();

            rs = statement.executeQuery(SQL_GET_ALL_GROUP);
            while (rs.next()){
                Group group = extractGroup(rs);
                groups.add(group);
            }
            return groups;
        }catch (SQLException ex){
            throw new DBException("Cannot find all groups in table groups", ex);
        }finally {
            close(connection);
            close(statement);
            close(rs);
        }
    }


    public boolean insertGroup(Group group) throws DBException {
        boolean res = false;
        Connection connection = null;
        PreparedStatement preparedStatement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(SQL_INSERT_GROUP, Statement.RETURN_GENERATED_KEYS);
            int k = 1;
            preparedStatement.setString(k++, group.getName());

            if (preparedStatement.executeUpdate() > 0){
                rs = preparedStatement.getGeneratedKeys();
                if (rs.next()){
                    group.setGroupId(rs.getInt(1));
                    res = true;
                }
            }

        } catch (SQLException e) {
            throw new DBException("Cannot insert group: " +  group, e);
        }finally {
            close(connection);
            close(preparedStatement);
            close(rs);
        }

        return res;
    }




    public User findUserByLogin(String login) throws SQLException {
        Connection connection =getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_USER_BY_LOGIN);

        int l = 1;
        preparedStatement.setString(l++, login);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            return extractUser(rs);
        }
        return null;
    }

    public Group findGroupByName(String name) throws SQLException {
        Connection connection =getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(SQL_FIND_GROUP_BY_NAME);

        int l = 1;
        preparedStatement.setString(l++, name);

        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            return extractGroup(rs);
        }
        return null;
    }


    public void setGroupsForUser(User userLogin, Group... groupName)throws SQLException{
        Connection connection = null;
        PreparedStatement preparedStatement= null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            preparedStatement = connection.prepareStatement(SQL_INSERT_USER_GROUP);

            if (userLogin == null || groupName == null){
                throw new NullPointerException("\"Don't exist group or user. Check your users\"");
            }

            for (int j = 0; j < groupName.length; j++){
                    int k = 1;
                    preparedStatement.setString(k++, userLogin.getLogin());
                    preparedStatement.setString(k++, groupName[j].getName());
                    preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

            connection.commit();

        } catch (SQLException e) {
            e.printStackTrace();
            if (connection != null) {
                connection.rollback();
            }
        }finally {
            close(connection);
            close(preparedStatement);
        }
    }

    public List<UserGroup> getUserGroup() throws SQLException, DBException {
        List<UserGroup> groups = new ArrayList<>();
        Connection connection = null;
        Statement statement= null;
        ResultSet rs= null;
        try {
            connection = getConnection();
            statement = connection.createStatement();

            rs = statement.executeQuery(SQL_GET_USER_GROUP);
            while (rs.next()){
                UserGroup userGroup = extractUserGroup(rs);
                groups.add(userGroup);
            }
            return groups;
        }catch (SQLException ex){
            throw new DBException("Cannot find all user group in table users_groups", ex);
        }finally {
            close(connection);
            close(statement);
            close(rs);
        }
    }

//    public boolean deleteUser(int id) throws SQLException, DBException {
//        boolean res = false;
//
//        Connection connection = null;
//        PreparedStatement preparedStatement = null;
//        ResultSet rs = null;
//
//        try {
//            connection = getConnection();
//            preparedStatement = connection.prepareStatement(SQL_DELETE_USER);
//            preparedStatement.setInt(1, id);
//            res = preparedStatement.executeUpdate() > 0;
//        }catch (SQLException e){
//            throw new DBException("Cannot delete a user with id:" +  id, e);
//        }finally {
//            close(connection);
//            close(preparedStatement);
//            close(rs);
//        }
//
//        return res;
//    }
//
//    public boolean updateUser(User user) throws DBException {
//        boolean res = false;
//
//        Connection con = null;
//        PreparedStatement pstmt = null;
//        ResultSet rs = null;
//
//        try {
//            con = getConnection();
//            pstmt = con.prepareStatement(SQL_UPDATE_USER);
//
//            int k = 1;
//            pstmt.setString(k++, user.getLogin());
//            pstmt.setInt(k++, user.getUserId());
//
//            res = pstmt.executeUpdate() > 0;
//        } catch (SQLException ex) {
//            throw new DBException("Cannot update a user:" +  user, ex);
//        } finally {
//            close(con);
//            close(pstmt);
//            close(rs);
//        }
//        return res;
//
//    }

    private UserGroup extractUserGroup(ResultSet rs) throws SQLException {
        UserGroup userGroup = new UserGroup();

        userGroup.setUserId(rs.getInt("user_id"));
        userGroup.setGroupId(rs.getInt("group_id"));
        userGroup.setName(rs.getString("name"));

        return userGroup;
    }

    public Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(CONNECTION_URL);

        return connection;
    }

    private void close(AutoCloseable ac) {
        if (ac != null) {
            try {
                ac.close();
            } catch (Exception ex) {
                throw new IllegalStateException("Cannot close " + ac);
            }
        }
    }

}
