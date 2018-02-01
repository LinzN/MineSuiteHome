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

import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.socket.JClientHomeOutput;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SetHomeCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public SetHomeCommand(HomePlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmnd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.sethome")) {
            this.executorServiceCommands.submit(() -> {
                int limit = 1;
                if (player.hasPermission("mineSuite.home.limit.admin")) {
                    limit = 10;
                } else if (player.hasPermission("mineSuite.home.limit.9")) {
                    limit = 9;
                } else if (player.hasPermission("mineSuite.home.limit.8")) {
                    limit = 8;
                } else if (player.hasPermission("mineSuite.home.limit.7")) {
                    limit = 7;
                } else if (player.hasPermission("mineSuite.home.limit.6")) {
                    limit = 6;
                } else if (player.hasPermission("mineSuite.home.limit.5")) {
                    limit = 5;
                } else if (player.hasPermission("mineSuite.home.limit.4")) {
                    limit = 4;
                } else if (player.hasPermission("mineSuite.home.limit.3")) {
                    limit = 3;
                } else if (player.hasPermission("mineSuite.home.limit.2")) {
                    limit = 2;
                }
                String homeName = "home";
                Location coords = player.getLocation();
                if (args.length == 1) {
                    homeName = args[0].toLowerCase();
                }
                JClientHomeOutput.sendHomeCreate(player.getUniqueId(), homeName, coords, limit);
            });
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
