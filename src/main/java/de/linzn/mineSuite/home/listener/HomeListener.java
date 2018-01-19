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

package de.linzn.mineSuite.home.listener;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.HomeDataTable;
import de.linzn.mineSuite.home.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class HomeListener implements Listener {

    @EventHandler
    public void playerConnect(PlayerSpawnLocationEvent e) {
        if (HomeDataTable.pendingHome.containsKey(e.getPlayer().getName())) {
            Player t = HomeDataTable.pendingHome.get(e.getPlayer().getName());
            HomeDataTable.pendingHome.remove(e.getPlayer().getName());
            if ((t == null) || (!t.isOnline())) {
                e.getPlayer().sendMessage("Player is no longer online");
                return;
            }
            HomeDataTable.ignoreHome.add(e.getPlayer());
            e.setSpawnLocation(t.getLocation());
            sendWarpMSG(e.getPlayer());
        } else if (HomeDataTable.pendingHomeLocations.containsKey(e.getPlayer().getName())) {
            Location l = HomeDataTable.pendingHomeLocations.get(e.getPlayer().getName());
            HomeDataTable.ignoreHome.add(e.getPlayer());
            e.setSpawnLocation(l);
            sendWarpMSG(e.getPlayer());
        }
    }

    public void sendWarpMSG(final Player p) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(HomePlugin.inst(), () -> {
            HomeDataTable.ignoreHome.remove(p);
            p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Home);
        }, 20);

    }
}
