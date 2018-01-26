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
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
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

public class HomeCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public HomeCommand(HomePlugin instance) {

    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.home")) {
            this.executorServiceCommands.submit(() -> {
                String homeName = "home";
                if ((args.length == 1)) {
                    homeName = args[0].toLowerCase();
                }
                final String finalHome = homeName;

                if (!player.hasPermission("mineSuite.bypass")) {
                    PendingTeleportsData.checkMoveLocation.put(player.getUniqueId(), player.getLocation());
                    player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}",
                            String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
                    HomePlugin.inst().getServer().getScheduler().runTaskLater(HomePlugin.inst(),
                            () -> {
                                Location loc = PendingTeleportsData.checkMoveLocation.get(player.getUniqueId());
                                PendingTeleportsData.checkMoveLocation.remove(player.getUniqueId());
                                if ((loc != null)
                                        && (loc.getBlock().equals(player.getLocation().getBlock()))) {
                                    JClientHomeOutput.sendTeleportToHomeOut(player.getUniqueId(), finalHome);
                                } else {
                                    player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);

                                }
                            }, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                } else {
                    JClientHomeOutput.sendTeleportToHomeOut(player.getUniqueId(), finalHome);
                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
