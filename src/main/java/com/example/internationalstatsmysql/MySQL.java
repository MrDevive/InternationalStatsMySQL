package com.example.internationalstatsmysql;

import java.sql.*;
import java.util.UUID;

public class MySQL {
    private Connection connection;
    private final ConfigManager config;

    public MySQL(ConfigManager config) {
        this.config = config;
    }

    public void connect() throws SQLException {
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase() +
                "?useSSL=false&autoReconnect=true";
        connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + config.getTable() + " (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "player_uuid VARCHAR(36) NOT NULL UNIQUE, " +
                "player_name VARCHAR(16) NOT NULL, " +
                "user_staff VARCHAR(20) DEFAULT 'user', " +
                "mob_kills INT DEFAULT 0, " +
                "player_kills INT DEFAULT 0, " +
                "deaths INT DEFAULT 0, " +
                "playtime_seconds BIGINT DEFAULT 0, " +
                "last_update TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                ")";
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);

            // Добавляем колонку если её нет (для существующих таблиц)
            addColumnIfNotExists("user_staff", "VARCHAR(20) DEFAULT 'user'");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveStats(UUID uuid, String name, String staffRole, int mobKills, int playerKills, int deaths, long playtimeSeconds) {
        String sql = "INSERT INTO " + config.getTable() +
                " (player_uuid, player_name, user_staff, mob_kills, player_kills, deaths, playtime_seconds) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE " +
                "player_name = VALUES(player_name), " +
                "user_staff = VALUES(user_staff), " +
                "mob_kills = VALUES(mob_kills), " +
                "player_kills = VALUES(player_kills), " +
                "deaths = VALUES(deaths), " +
                "playtime_seconds = VALUES(playtime_seconds)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, uuid.toString());
            stmt.setString(2, name);
            stmt.setString(3, staffRole);
            stmt.setInt(4, mobKills);
            stmt.setInt(5, playerKills);
            stmt.setInt(6, deaths);
            stmt.setLong(7, playtimeSeconds);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addColumnIfNotExists(String columnName, String columnDefinition) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet columns = metaData.getColumns(null, null, config.getTable(), columnName);

            if (!columns.next()) {
                // Колонки нет — добавляем
                String alterSql = "ALTER TABLE " + config.getTable() + " ADD COLUMN " + columnName + " " + columnDefinition;
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(alterSql);
                    System.out.println("Added column: " + columnName);
                }
            }
            columns.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}