package com.christopher.javaserver.db;

import java.sql.*;

public class Connector {

    private static final Connector INSTANCE = new Connector();
    private static Connection connection;

    private Connector() {
    }

    private static void setInstance(String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection(url, user, password);
    }

    /**
     * Thread safe singleton access with lazy initialization. And that's the most loaded sentence i've ever written
     *
     * @param url      the connection url eg "jdbc:mysql://localhost:3306/db_name
     * @param user     user to connect as
     * @param password Password for said user
     * @return the Connection object used for queries
     */
    public static Connector getInstance(String url, String user, String password) throws SQLException, ClassNotFoundException {
        if (connection == null) {
            synchronized (Connector.class) {
                setInstance(url, user, password);
            }
        }
        return INSTANCE;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(query);
        return stmt.executeQuery(query);
    }

}
