package com.example.internationalstatsmysql;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class InternationalStatsMySQL extends JavaPlugin implements Listener {

    private MySQL mysql;
    private ConfigManager configManager;
    private LuckPermsManager luckPermsManager;
    private final Map<UUID, Long> joinTimes = new HashMap<>();

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        mysql = new MySQL(configManager);

        // Проверяем наличие LuckPerms
        if (getServer().getPluginManager().getPlugin("LuckPerms") == null) {
            getLogger().severe("LuckPerms not found! Disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        luckPermsManager = new LuckPermsManager();
        getLogger().info("LuckPerms found and integrated!");

        try {
            mysql.connect();
            mysql.createTable();
            getLogger().info("Connected to MySQL successfully!");
        } catch (SQLException e) {
            getLogger().severe("Failed to connect to MySQL: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getServer().getPluginManager().registerEvents(this, this);

        // Сохраняем статистику всех онлайн игроков каждые 5 минут
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : getServer().getOnlinePlayers()) {
                    StatsCollector.updateAndSave(p, mysql, luckPermsManager);
                }
            }
        }.runTaskTimerAsynchronously(this, 20 * 60 * 5, 20 * 60 * 5);

        getLogger().info("InternationalStatsMySQL enabled");
    }

    @Override
    public void onDisable() {
        // Сохраняем статистику перед выключением
        for (Player p : getServer().getOnlinePlayers()) {
            StatsCollector.updateAndSave(p, mysql, luckPermsManager);
        }
        mysql.close();
        getLogger().info("InternationalStatsMySQL disabled");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        joinTimes.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
        // При входе тоже обновляем статистику
        StatsCollector.updateAndSave(event.getPlayer(), mysql, luckPermsManager);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player p = event.getPlayer();
        StatsCollector.updateAndSave(p, mysql, luckPermsManager);
        joinTimes.remove(p.getUniqueId());
    }
}