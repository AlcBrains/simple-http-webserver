package com.christopher.javaserver.db;

import java.sql.*;

public class Connector {

    private static volatile Connection INSTANCE = null;

    private void setInstance(String url, String user, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        INSTANCE = DriverManager.getConnection(url, user, password);
    }

    /**
     * Thread safe singleton access with lazy initialization. And that's the most loaded
     *
     * @param url      the connection url eg "jdbc:mysql://localhost:3306/db_name
     * @param user     user to connect as
     * @param password Password for said user
     * @return the Connection object used for queries
     */
    public Connection getInstance(String url, String user, String password) throws SQLException, ClassNotFoundException {
        if (INSTANCE == null) {
            synchronized (Connector.class) {
                if (INSTANCE == null) {
                    this.setInstance(url, user, password);
                }
            }
        }
        return INSTANCE;
    }

    public ResultSet executeQuery(Connection con, String query) throws SQLException {
        Statement stmt = con.createStatement();
        return stmt.executeQuery(query);
    }
}
