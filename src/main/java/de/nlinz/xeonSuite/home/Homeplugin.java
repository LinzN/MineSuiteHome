package de.nlinz.xeonSuite.home;

import org.bukkit.plugin.java.JavaPlugin;

import de.nlinz.xeonSuite.home.commands.DeleteHomeCommand;
import de.nlinz.xeonSuite.home.commands.HomeCommand;
import de.nlinz.xeonSuite.home.commands.HomeListCommand;
import de.nlinz.xeonSuite.home.commands.SetHomeCommand;
import de.nlinz.xeonSuite.home.database.MineHomeDB;
import de.nlinz.xeonSuite.home.listener.BukkitSockHomeListener;
import de.nlinz.xeonSuite.home.listener.HomeListener;

public class Homeplugin extends JavaPlugin {
	private static Homeplugin inst;

	public void onEnable() {
		inst = this;

		if (MineHomeDB.create()) {
			// someting
		}
		loadCommands();
		getServer().getPluginManager().registerEvents(new BukkitSockHomeListener(), this);
		getServer().getPluginManager().registerEvents(new HomeListener(), this);
	}

	public void onDisable() {
	}

	public static Homeplugin inst() {
		return inst;
	}

	public void loadCommands() {
		getCommand("home").setExecutor(new HomeCommand(this));
		getCommand("sethome").setExecutor(new SetHomeCommand(this));
		getCommand("homes").setExecutor(new HomeListCommand(this));
		getCommand("delhome").setExecutor(new DeleteHomeCommand(this));
	}

}
