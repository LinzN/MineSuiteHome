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

package de.linzn.mineSuite.home.api;


import de.linzn.mineSuite.core.configurations.YamlFiles.GeneralLanguage;
import de.linzn.mineSuite.core.database.hashDatabase.PendingTeleportsData;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.home.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.UUID;

public class HomeManager {

    public static void teleportToHome(UUID playerUUID, String world, double x, double y, double z, float yaw,
                                      float pitch) {
        World w = Bukkit.getWorld(world);
        Location t;

        if (w != null) {
            t = new Location(w, x, y, z, yaw, pitch);
        } else {
            w = Bukkit.getWorlds().get(0);
            t = w.getSpawnLocation();
        }
        Player p = Bukkit.getPlayer(playerUUID);

        if (p != null) {
            Bukkit.getScheduler().runTask(HomePlugin.inst(), () -> {
                // Check if Block is safe
                if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
                    try {
                        Location l = LocationUtil.getSafeDestination(p, t);
                        if (l != null) {
                            p.teleport(l);
                            p.sendMessage(GeneralLanguage.teleport_success);
                        } else {
                            p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.teleport(t);
                    p.sendMessage(GeneralLanguage.teleport_success);
                }
            });
        } else {
            PendingTeleportsData.pendingLocations.put(playerUUID, t);

            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(HomePlugin.inst(), () -> {
                if (PendingTeleportsData.pendingLocations.containsKey(playerUUID)) {
                    PendingTeleportsData.pendingLocations.remove(playerUUID);
                }
            }, 100L);
        }
    }
}
