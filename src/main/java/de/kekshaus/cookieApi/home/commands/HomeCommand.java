package de.kekshaus.cookieApi.home.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.home.Homeplugin;
import de.kekshaus.cookieApi.home.api.HOStreamOutApi;
import de.kekshaus.cookieApi.home.database.ConnectionInject;
import de.kekshaus.cookieApi.home.database.HomeHASHDB;
import net.md_5.bungee.api.ChatColor;

public class HomeCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public HomeCommand(Homeplugin instance) {

	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.home.home")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {

						String homeName = "home";
						if ((args.length == 1)) {
							homeName = args[0].toLowerCase();
						}

						if (ConnectionInject.isHome(player.getUniqueId(), homeName)) {
							if (!player.hasPermission("cookieApi.bypass")) {
								HomeHASHDB.lastHomeLocation.put(player, player.getLocation());
								player.sendMessage(MessageDB.TELEPORT_TIMER.replace("{TIME}",
										String.valueOf(CookieApiBukkit.getWarmUpTime())));
								Homeplugin.inst().getServer().getScheduler().runTaskLater(Homeplugin.inst(),
										new Runnable() {
									@Override
									public void run() {
										String homelName = "home";
										if ((args.length == 1)) {
											homelName = args[0].toLowerCase();
										}
										Location loc = HomeHASHDB.lastHomeLocation.get(player);
										HomeHASHDB.lastHomeLocation.remove(player);
										if ((loc != null) && (loc.getBlock().equals(player.getLocation().getBlock()))) {
											List<String> list = ConnectionInject.getHome(player.getUniqueId(),
													homelName);
											String world = list.get(1);
											String server = list.get(2);
											double x = Double.parseDouble(list.get(3));
											double y = Double.parseDouble(list.get(4));
											double z = Double.parseDouble(list.get(5));
											float yaw = Float.parseFloat(list.get(6));
											float pitch = Float.parseFloat(list.get(7));
											HOStreamOutApi.sendTeleportToHomeOut(player.getName(), server, world, x, y,
													z, yaw, pitch);
											return;
										} else {
											player.sendMessage(MessageDB.TELEPORT_MOVE_CANCEL);

										}
									}
								}, 20L * CookieApiBukkit.getWarmUpTime());
							} else {
								List<String> list = ConnectionInject.getHome(player.getUniqueId(), homeName);
								String world = list.get(1);
								String server = list.get(2);
								double x = Double.parseDouble(list.get(3));
								double y = Double.parseDouble(list.get(4));
								double z = Double.parseDouble(list.get(5));
								float yaw = Float.parseFloat(list.get(6));
								float pitch = Float.parseFloat(list.get(7));
								HOStreamOutApi.sendTeleportToHomeOut(player.getName(), server, world, x, y, z, yaw,
										pitch);
								return;

							}

						} else {
							player.sendMessage(
									ChatColor.GOLD + "Du besitzt keine Homes oder dieses Home existiert nicht!");
						}
					}
				}
			});
		} else {
			sender.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
