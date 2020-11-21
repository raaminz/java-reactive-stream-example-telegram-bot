package com.github.zare88.websearch.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Objects;

public class Requests {

    private Requests() {
    }

    public static class DDL {

        private DDL() {

        }

        public static void createTable() {
            try (Connection connection = DBConnection.INSTANCE.getConnection()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute("CREATE TABLE IF NOT EXISTS REQUESTS (" +
                            "ID INTEGER NOT NULL AUTO_INCREMENT," +
                            "CHAT_ID INTEGER NOT NULL," +
                            "REQUEST_DATE TIMESTAMP NOT NULL," +
                            "TEXT VARCHAR(500) NOT NULL" +
                            ");");
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static class DAO {

        private DAO() {

        }

        public static void insert(LocalDateTime dateTime, Long chatId, String text) {
            Objects.requireNonNull(dateTime);
            Objects.requireNonNull(text);
            String sql = "INSERT INTO REQUESTS (REQUEST_DATE, CHAT_ID, TEXT) VALUES(?,?,?)";
            try (Connection connection = DBConnection.INSTANCE.getConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setObject(1, dateTime);
                    statement.setLong(2, chatId);
                    statement.setString(3, text);
                    statement.executeUpdate();
                }
            } catch (SQLException e) {
                throw new IllegalStateException(e);
            }
        }

    }
}
