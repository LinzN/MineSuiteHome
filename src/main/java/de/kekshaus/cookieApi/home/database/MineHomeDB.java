package de.kekshaus.cookieApi.home.database;

import java.sql.Connection;
import java.sql.Statement;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import de.kekshaus.cookieApi.bukkit.CookieApiBukkit;
import de.kekshaus.cookieApi.home.Homeplugin;

public class MineHomeDB {
	public static boolean create() {
		return mysql();

	}

	public static boolean mysql() {
		String db = CookieApiBukkit.getDataBase();
		String port = CookieApiBukkit.getPort();
		String host = CookieApiBukkit.getHost();
		String url = "jdbc:mysql://" + host + ":" + port + "/" + db;
		String username = CookieApiBukkit.getUsername();
		String password = CookieApiBukkit.getPassword();
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
			if (CookieApiBukkit.isDebugmode()) {
				e.printStackTrace();
			}
			return false;
		}

	}

}
