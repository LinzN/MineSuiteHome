/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.home.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.database.HomeSqlActions;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class DeleteHomeCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public DeleteHomeCommand(HomePlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.delhome")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    Player player1 = (Player) sender;
                    String homeName = "home";
                    if ((args.length == 1)) {
                        homeName = args[0].toLowerCase();
                    }
                    HomeSqlActions.delHome(player1.getUniqueId(), homeName);
                    sender.sendMessage(ChatColor.GREEN + "Das Home " + ChatColor.YELLOW + homeName + ChatColor.GREEN
                            + " wurde entfernt.");
                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
