package com.example.internationalstatsmysql;

import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class StatsCollector {

    public static int getMobKills(Player p) {
        return p.getStatistic(Statistic.MOB_KILLS);
    }

    public static int getPlayerKills(Player p) {
        return p.getStatistic(Statistic.PLAYER_KILLS);
    }

    public static int getDeaths(Player p) {
        return p.getStatistic(Statistic.DEATHS);
    }

    public static long getPlaytimeSeconds(Player p) {
        return p.getStatistic(Statistic.PLAY_ONE_MINUTE) / 20;
    }

    public static void updateAndSave(Player p, MySQL mysql, LuckPermsManager luckPerms) {
        UUID uuid = p.getUniqueId();
        String name = p.getName();
        String staffRole = luckPerms.getUserStaffRole(p);
        int mobKills = getMobKills(p);
        int playerKills = getPlayerKills(p);
        int deaths = getDeaths(p);
        long playtime = getPlaytimeSeconds(p);

        mysql.saveStats(uuid, name, staffRole, mobKills, playerKills, deaths, playtime);
    }
}