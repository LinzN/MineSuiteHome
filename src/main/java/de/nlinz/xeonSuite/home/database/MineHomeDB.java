package de.nlinz.xeonSuite.home.database;

import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.home.Homeplugin;

public class MineHomeDB {
	public static boolean create() {
		return mysql();

	}

	public static boolean mysql() {
		String db = XeonSuiteBukkit.getDataBase();
		String port = XeonSuiteBukkit.getPort();
		String host = XeonSuiteBukkit.getHost();
		String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
		String username = XeonSuiteBukkit.getUsername();
		String password = XeonSuiteBukkit.getPassword();
		ConnectionFactory factory = new ConnectionFactory(url, username, password);
		ConnectionManager manager = ConnectionManager.DEFAULT;
		ConnectionHandler handler = manager.getHandler("minehome", factory);

		try {
			Connection connection = handler.getConnection();
			String sql = "CREATE TABLE IF NOT EXISTS homes (player VARCHAR(100), home_name VARCHAR(100), server VARCHAR(100), world text, x double, y double, z double, yaw float, pitch float, PRIMARY KEY (`player`,`home_name`,`server`));";
			Statement action = connection.createStatement();
			action.executeUpdate(sql);
			action.close();
			handler.release(connection);
			Homeplugin.inst().getLogger().info("[Module] Database loaded!");
			return true;

		} catch (Exception e) {
			Homeplugin.inst().getLogger().info("[Module] Database error!");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "==================HOME-ERROR================");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unable to connect to database.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Pls check you mysql connection in config.yml.");
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "==================HOME-ERROR================");
			if (XeonSuiteBukkit.isDebugmode()) {
				e.printStackTrace();
			}
			return false;
		}

	}

}
