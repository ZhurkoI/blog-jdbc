package org.zhurko.blog.repository.sql;

import org.zhurko.blog.util.PropertyUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionBuilder {

    private static final String dbUrl;
    private static final String dbUser;
    private static final String dbPassword;

    static {
        dbUrl = PropertyUtil.getProperty("db.url");
        dbUser = PropertyUtil.getProperty("db.user");
        dbPassword = PropertyUtil.getProperty("db.password");
    }

    private ConnectionBuilder() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }
}
