package de.nlinz.xeonSuite.home.commands;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.bukkit.utils.tables.HomeDataTable;
import de.nlinz.xeonSuite.bukkit.GlobalMessageDB;
import de.nlinz.xeonSuite.home.Homeplugin;
import de.nlinz.xeonSuite.home.api.HOStreamOutApi;
import de.nlinz.xeonSuite.home.database.HomeSqlActions;
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

						if (HomeSqlActions.isHome(player.getUniqueId(), homeName)) {
							if (!player.hasPermission("cookieApi.bypass")) {
								HomeDataTable.lastHomeLocation.put(player, player.getLocation());
								player.sendMessage(GlobalMessageDB.TELEPORT_TIMER.replace("{TIME}",
										String.valueOf(XeonSuiteBukkit.getWarmUpTime())));
								Homeplugin.inst().getServer().getScheduler().runTaskLater(Homeplugin.inst(),
										new Runnable() {
											@Override
											public void run() {
												String homelName = "home";
												if ((args.length == 1)) {
													homelName = args[0].toLowerCase();
												}
												Location loc = HomeDataTable.lastHomeLocation.get(player);
												HomeDataTable.lastHomeLocation.remove(player);
												if ((loc != null)
														&& (loc.getBlock().equals(player.getLocation().getBlock()))) {
													List<String> list = HomeSqlActions.getHome(player.getUniqueId(),
															homelName);
													String world = list.get(1);
													String server = list.get(2);
													double x = Double.parseDouble(list.get(3));
													double y = Double.parseDouble(list.get(4));
													double z = Double.parseDouble(list.get(5));
													float yaw = Float.parseFloat(list.get(6));
													float pitch = Float.parseFloat(list.get(7));
													HOStreamOutApi.sendTeleportToHomeOut(player.getName(), server,
															world, x, y, z, yaw, pitch);
													return;
												} else {
													player.sendMessage(GlobalMessageDB.TELEPORT_MOVE_CANCEL);

												}
											}
										}, 20L * XeonSuiteBukkit.getWarmUpTime());
							} else {
								List<String> list = HomeSqlActions.getHome(player.getUniqueId(), homeName);
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
			sender.sendMessage(GlobalMessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
