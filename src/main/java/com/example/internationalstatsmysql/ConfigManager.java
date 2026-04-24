package com.example.internationalstatsmysql;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ConfigManager {
    private final JavaPlugin plugin;
    private String host;
    private int port;
    private String database;
    private String username;
    private String password;
    private String table;

    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        this.host = config.getString("mysql.host", "127.0.1.31");
        this.port = config.getInt("mysql.port", 3306);
        this.database = config.getString("mysql.database", "international_minecraft_auth");
        this.username = config.getString("mysql.username", "root");
        this.password = config.getString("mysql.password", "root");
        this.table = config.getString("mysql.table", "player_stats");
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getDatabase() { return database; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getTable() { return table; }
}