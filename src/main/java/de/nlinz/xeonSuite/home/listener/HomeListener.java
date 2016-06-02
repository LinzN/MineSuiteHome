package de.nlinz.xeonSuite.home.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.home.database.HomeHASHDB;

public class HomeListener implements Listener {

	@EventHandler
	public void playerConnect(PlayerSpawnLocationEvent e) {
		if (HomeHASHDB.pendingHome.containsKey(e.getPlayer().getName())) {
			Player t = HomeHASHDB.pendingHome.get(e.getPlayer().getName());
			HomeHASHDB.pendingHome.remove(e.getPlayer().getName());
			if ((t == null) || (!t.isOnline())) {
				e.getPlayer().sendMessage("Player is no longer online");
				return;
			}
			HomeHASHDB.ignoreHome.add(e.getPlayer());
			e.setSpawnLocation(t.getLocation());
			sendWarpMSG(e.getPlayer());
		} else if (HomeHASHDB.pendingHomeLocations.containsKey(e.getPlayer().getName())) {
			Location l = HomeHASHDB.pendingHomeLocations.get(e.getPlayer().getName());
			HomeHASHDB.ignoreHome.add(e.getPlayer());
			e.setSpawnLocation(l);
			sendWarpMSG(e.getPlayer());
		}
	}

	public void sendWarpMSG(final Player p) {
		Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
			@Override
			public void run() {
				HomeHASHDB.ignoreHome.remove(p);
				p.sendMessage(GlobalMessageDB.Teleport_Home);
			}
		}, 20);

	}
}
