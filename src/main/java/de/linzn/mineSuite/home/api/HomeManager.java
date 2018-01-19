package de.linzn.mineSuite.home.api;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.HomeDataTable;
import de.linzn.mineSuite.core.utils.LocationUtil;
import de.linzn.mineSuite.home.HomePlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class HomeManager {

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
            Bukkit.getScheduler().runTask(HomePlugin.inst(), () -> {
                // Check if Block is safe
                if (LocationUtil.isBlockUnsafe(t.getWorld(), t.getBlockX(), t.getBlockY(), t.getBlockZ())) {
                    try {
                        Location l = LocationUtil.getSafeDestination(p, t);
                        if (l != null) {
                            p.teleport(l);
                            p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Home);
                            return;
                        } else {
                            p.sendMessage(ChatColor.RED + "Unable to find a safe location for teleport.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    p.teleport(t);
                    p.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.Teleport_Home);
                    return;
                }
            });
        } else {

            HomeDataTable.pendingHomeLocations.put(player, t);

            // clear pending teleport if they dont connect
            Bukkit.getScheduler().runTaskLaterAsynchronously(HomePlugin.inst(), () -> {
                if (HomeDataTable.pendingHomeLocations.containsKey(player)) {
                    HomeDataTable.pendingHomeLocations.remove(player);
                }
            }, 100L);
        }
    }
}
