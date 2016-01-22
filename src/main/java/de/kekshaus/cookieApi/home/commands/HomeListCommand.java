package de.kekshaus.cookieApi.home.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.kekshaus.cookieApi.bukkit.MessageDB;
import de.kekshaus.cookieApi.home.Homeplugin;
import de.kekshaus.cookieApi.home.database.ConnectionInject;

public class HomeListCommand implements CommandExecutor {
	public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
			new LinkedBlockingQueue<Runnable>());

	public HomeListCommand(Homeplugin instance) {
	}

	public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
		final Player player = (Player) sender;
		if (player.hasPermission("cookieApi.home.homes")) {
			this.executorServiceCommands.submit(new Runnable() {
				public void run() {
					if (sender instanceof Player) {
						Player p = (Player) sender;

						HashMap<String, String> list = ConnectionInject.getHomes(p.getUniqueId());
						List<String> homename = new ArrayList<String>();
						List<String> servername = new ArrayList<String>();

						for (Map.Entry<String, String> s : list.entrySet()) {
							homename.add(s.getKey());
							servername.add(s.getValue());
						}

						int pageNumb = 0;
						try {
							if (args.length == 1) {
								int number = Integer.valueOf(args[0]);
								if (number < 1) {
									pageNumb = 0;
								} else {
									pageNumb = Integer.valueOf(args[0]) - 1;
								}

							} else {
								pageNumb = 0;
							}
						} catch (Exception e) {
							p.sendMessage("No number");
							return;
						}
						int counter = 1;
						int rgCount = list.size();
						if ((pageNumb * 6 + 1) > rgCount) {
							sender.sendMessage("So viele Homeseiten besitzt du nicht!");
							return;
						}
						Collections.sort(homename);
						sender.sendMessage("§aAuflistung all deiner Homes: ");
						sender.sendMessage("§aServername?   §9Homename? ");
						List<String> homelist = homename.subList(pageNumb * 6,
								pageNumb * 6 + 6 > rgCount ? rgCount : pageNumb * 6 + 6);
						for (String s : homelist) {
							sender.sendMessage("§a" + list.get(s) + ": §9" + s);
							counter++;
						}
						if (counter >= 7) {

							int pageSeite;
							if (pageNumb == 0) {
								pageSeite = 2;
							} else {
								pageSeite = (pageNumb + 2);
							}
							sender.sendMessage("§aMehr auf Seite §e" + pageSeite + " §amit §e/homes " + pageSeite);
						}

						if (counter <= 6 && pageNumb != 0) {
							int pageSeite = (pageNumb);

							sender.sendMessage("§aZurück auf Seite §e" + pageSeite + "§a mit §e/homes " + pageSeite);
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
