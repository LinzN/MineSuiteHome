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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HomeListCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public HomeListCommand(HomePlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.homes")) {
            this.executorServiceCommands.submit(() -> {
                Player p = (Player) sender;
                int pageNumb;
                try {
                    if (args.length == 1) {
                        int number = Integer.valueOf(args[0]);
                        if (number < 1) {
                            pageNumb = 0;
                        } else {
                            pageNumb = Integer.valueOf(args[0]) - 1;
                        }
                    } else {
                        pageNumb = 0;
                    }
                    JClientHomeOutput.sendGetHomesList(player.getUniqueId(), pageNumb);
                } catch (Exception e) {
                    p.sendMessage(GeneralLanguage.global_NO_NUMBER);
                }
            });
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
