package com.christopher.javaserver.db;

import java.sql.*;

public class Connector {

    private static final Connector INSTANCE = new Connector();
    private static Connection connection;
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    private Connector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            //todo: add logging message
        }
    }

    /**
     * Thread safe singleton access with lazy initialization. And that's the most loaded sentence i've ever written
     *
     * @param url      the connection url eg "jdbc:mysql://localhost:3306/db_name
     * @param user     user to connect as
     * @param password Password for said user
     * @return the Connection object used for queries
     */
    public static Connector getInstance(String url, String user, String password) {
        URL = url;
        USER = user;
        PASSWORD = password;
        try {
            if (connection == null || connection.isClosed()) {
                synchronized (Connector.class) {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        } catch (SQLException e) {
            //todo: add logging error
            e.printStackTrace();
            System.exit(1);
        }
        return INSTANCE;
    }

    public ResultSet executeQuery(String query) {

        try {
            if (connection.isClosed()) {
                getInstance(URL, USER, PASSWORD);
            }
            PreparedStatement stmt = connection.prepareStatement(query);
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            //todo : log sql exception error
            closeConnection();
            return null;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            // todo:add logger message
        }

    }

}
