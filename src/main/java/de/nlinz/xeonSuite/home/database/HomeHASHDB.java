package de.nlinz.xeonSuite.home.database;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class HomeHASHDB {
	public static HashMap<String, Player> pendingHome = new HashMap<String, Player>();
	public static HashMap<String, Location> pendingHomeLocations = new HashMap<String, Location>();
	public static HashSet<Player> ignoreHome = new HashSet<Player>();
	public static HashMap<Player, Location> lastHomeLocation = new HashMap<Player, Location>();

	public static void RemovePlayer(Player player) {
		pendingHome.remove(player.getName());
		pendingHomeLocations.remove(player.getName());
		ignoreHome.remove(player);
		lastHomeLocation.remove(player);
	}

}