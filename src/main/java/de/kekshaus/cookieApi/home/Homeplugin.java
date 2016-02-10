package de.kekshaus.cookieApi.home;

import org.bukkit.plugin.java.JavaPlugin;

import de.kekshaus.cookieApi.home.commands.DeleteHomeCommand;
import de.kekshaus.cookieApi.home.commands.HomeCommand;
import de.kekshaus.cookieApi.home.commands.HomeListCommand;
import de.kekshaus.cookieApi.home.commands.SetHomeCommand;
import de.kekshaus.cookieApi.home.database.MineHomeDB;

public class Homeplugin extends JavaPlugin {
	private static Homeplugin inst;

	public void onEnable() {
		inst = this;

		if (MineHomeDB.create()) {
			// someting
		}
		loadCommands();
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
