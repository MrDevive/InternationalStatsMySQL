package com.example.internationalstatsmysql;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutionException;

public class LuckPermsManager {
    private final LuckPerms luckPerms;

    public LuckPermsManager() {
        this.luckPerms = LuckPermsProvider.get();
    }

    public String getUserStaffRole(Player player) {
        try {
            // Асинхронное получение пользователя с ожиданием
            User user = luckPerms.getUserManager().loadUser(player.getUniqueId()).get();
            if (user == null) return "user";

            String highestRole = "user";

            for (var node : user.getNodes()) {
                // Правильная проверка на группу (InheritanceNode)
                if (!(node instanceof InheritanceNode)) continue;

                String group = node.getKey().toLowerCase().replace("group.", "");

                switch (group) {
                    case "admin":
                        return "admin"; // Самая высшая роль, можно сразу вернуть
                    case "moder":
                    case "moderator":
                    case "mod":
                        if (!highestRole.equals("admin")) {
                            highestRole = "moder";
                        }
                        break;
                    case "helper":
                    case "help":
                        if (!highestRole.equals("admin") && !highestRole.equals("moder")) {
                            highestRole = "helper";
                        }
                        break;
                }
            }

            return highestRole;

        } catch (InterruptedException | ExecutionException e) {
            return "user";
        }
    }
}