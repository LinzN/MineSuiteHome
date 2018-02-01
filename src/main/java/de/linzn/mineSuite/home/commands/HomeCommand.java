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
import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.socket.JClientHomeOutput;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HomeCommand implements CommandExecutor {
    private ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public HomeCommand(HomePlugin instance) {

    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.home")) {
            if (!PendingTeleportsData.playerCommand.contains(player.getUniqueId())) {
                PendingTeleportsData.addCommandSpam(player.getUniqueId());
                this.executorServiceCommands.submit(() -> {
                    String homeName = "home";
                    if ((args.length == 1)) {
                        homeName = args[0].toLowerCase();
                    }
                    final String finalHome = homeName;
                    player.sendMessage(GeneralLanguage.teleport_TELEPORT_TIMER);
                    HomePlugin.inst().getServer().getScheduler().runTaskLaterAsynchronously(HomePlugin.inst(),
                            () -> JClientHomeOutput.sendTeleportToHomeOut(player.getUniqueId(), finalHome), (long) MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                });
            } else {
                player.sendMessage(GeneralLanguage.global_COMMAND_PENDING);
            }
        } else {
            player.sendMessage(GeneralLanguage.global_NO_PERMISSIONS);
        }
        return false;
    }
}
