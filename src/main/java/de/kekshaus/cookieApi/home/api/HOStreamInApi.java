package de.kekshaus.cookieApi.home.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.GlobalMessageDB;
import de.kekshaus.cookieApi.bukkit.utils.LocationUtil;
import de.kekshaus.cookieApi.home.database.HomeHASHDB;

public class HOStreamInApi {

	public static void teleportToHome(final String player, String world, double x, double y, double z, float yaw,
			float pitch) {
		World w = Bukkit.getWorld(world);
		Location t;

		if (w != null) {
			t = new Location(w, x, y, z, yaw, pitch);
		} else {
			w = Bukkit.getWorlds().get(0);
			t = w.getSpawnLocation();
		}
		Player p = Bukkit.getPlayer(player);

		if (p != null) {
			// Check if Block is safe
			if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
				try {
					Location l = LocationUtil.getSafeDestination(p, t);
					if (l != null) {
						p.teleport(l);
						p.sendMessage(GlobalMessageDB.Teleport_Home);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.teleport(t);
				p.sendMessage(GlobalMessageDB.Teleport_Home);
				return;
			}

		} else {

			HomeHASHDB.pendingHomeLocations.put(player, t);

			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(CookieApiBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (HomeHASHDB.pendingHomeLocations.containsKey(player)) {
						HomeHASHDB.pendingHomeLocations.remove(player);
					}
				}
			}, 100L);
		}
	}
}
