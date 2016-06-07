package de.nlinz.xeonSuite.home.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.LocationUtil;
import de.nlinz.xeonSuite.bukkit.utils.languages.HomeLanguage;
import de.nlinz.xeonSuite.bukkit.utils.tables.HomeDataTable;

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
						p.sendMessage(HomeLanguage.Teleport_Home);
						return;
					} else {
						p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				p.teleport(t);
				p.sendMessage(HomeLanguage.Teleport_Home);
				return;
			}

		} else {

			HomeDataTable.pendingHomeLocations.put(player, t);

			// clear pending teleport if they dont connect
			Bukkit.getScheduler().runTaskLaterAsynchronously(XeonSuiteBukkit.getInstance(), new Runnable() {
				@Override
				public void run() {
					if (HomeDataTable.pendingHomeLocations.containsKey(player)) {
						HomeDataTable.pendingHomeLocations.remove(player);
					}
				}
			}, 100L);
		}
	}
}
