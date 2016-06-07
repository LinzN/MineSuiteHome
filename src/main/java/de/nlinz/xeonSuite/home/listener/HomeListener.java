package de.nlinz.xeonSuite.home.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.languages.HomeLanguage;
import de.nlinz.xeonSuite.bukkit.utils.tables.HomeDataTable;

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
		Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				HomeDataTable.ignoreHome.remove(p);
				p.sendMessage(HomeLanguage.Teleport_Home);
			}
		}, 20);

	}
}
