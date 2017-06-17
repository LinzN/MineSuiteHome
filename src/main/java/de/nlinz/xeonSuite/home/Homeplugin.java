package de.nlinz.xeonSuite.home;

import org.bstats.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import de.nlinz.javaSocket.client.api.XeonSocketClientManager;
import de.nlinz.xeonSuite.home.commands.DeleteHomeCommand;
import de.nlinz.xeonSuite.home.commands.HomeCommand;
import de.nlinz.xeonSuite.home.commands.HomeListCommand;
import de.nlinz.xeonSuite.home.commands.SetHomeCommand;
import de.nlinz.xeonSuite.home.listener.HomeListener;
import de.nlinz.xeonSuite.home.listener.XeonHome;

public class Homeplugin extends JavaPlugin {
	private static Homeplugin inst;

	@Override
	public void onEnable() {
		inst = this;

		loadCommands();
		XeonSocketClientManager.registerDataListener(new XeonHome());
		getServer().getPluginManager().registerEvents(new HomeListener(), this);
		new Metrics(this);
	}

	@Override
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
