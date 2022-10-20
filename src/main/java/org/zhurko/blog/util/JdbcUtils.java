package org.zhurko.blog.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcUtils {

    private static Connection connection = null;
    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;

    static {
        dbUrl = PropertyUtil.getProperty("db.url");
        dbUser = PropertyUtil.getProperty("db.user");
        dbPassword = PropertyUtil.getProperty("db.password");
    }

    private JdbcUtils() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null) {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        }
        return connection;
    }

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException{
        return getConnection().prepareStatement(sql);
    }

    public static PreparedStatement getPreparedStatementWithGeneratedKeys(String sql) throws SQLException{
        return getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
    }
}
