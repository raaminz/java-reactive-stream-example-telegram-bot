package com.github.zare88.websearch.db;

import org.h2.jdbcx.JdbcConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {
    public static final DBConnection INSTANCE = new DBConnection();
    private final JdbcConnectionPool connectionPool;

    private DBConnection() {
        connectionPool = JdbcConnectionPool.
                create("jdbc:h2:mem:test", "sa", "sa");
    }

    public Connection getConnection() throws SQLException {
        return connectionPool.getConnection();
    }

}
