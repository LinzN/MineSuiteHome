package de.kekshaus.cookieApi.home.commands;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.home.Homeplugin;
import de.kekshaus.cookieApi.home.database.ConnectionInject;

public class DeleteHomeCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public DeleteHomeCommand(Homeplugin instance) {
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.home.delhome")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player player = (Player) sender;
						String homeName = "home";
						if ((args.length == 1)) {
							homeName = args[0].toLowerCase();
						}
						ConnectionInject.delHome(player.getUniqueId(), homeName);
						sender.sendMessage(ChatColor.GREEN + "Das Home " + ChatColor.YELLOW + homeName + ChatColor.GREEN
								+ " wurde entfernt.");
					}
				}
			});
		} else {
			sender.sendMessage(MessageDB.NO_PERMISSIONS);
		}
		return false;
	}
}
