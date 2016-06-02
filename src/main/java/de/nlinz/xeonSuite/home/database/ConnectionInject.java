package de.nlinz.xeonSuite.home.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ConnectionInject {

	public static void setHome(UUID uuid, String home, String server, String world, double x, double y, double z,
			float yaw, float pitch) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minehome");
			PreparedStatement sql = conn.prepareStatement("SELECT home_name FROM homes WHERE player = '"
					+ uuid.toString().replace("-", "") + "' AND home_name = '" + home + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("UPDATE homes SET server = '" + server + "', world = '"
						+ world + "', x = '" + x + "', y = '" + y + "', z = '" + z + "', yaw = '" + yaw + "', pitch = '"
						+ pitch + "' WHERE player = '" + uuid.toString().replace("-", "") + "' AND home_name = '" + home
						+ "';");
				update.executeUpdate();
				update.close();
			} else {
				PreparedStatement insert = conn.prepareStatement(
						"INSERT INTO homes (player, home_name, server, world, x, y, z, yaw, pitch) VALUES ('"
								+ uuid.toString().replace("-", "") + "', '" + home + "', '" + server + "', '" + world
								+ "', '" + x + "', '" + y + "', '" + z + "', '" + yaw + "', '" + pitch + "');");
				insert.executeUpdate();
				insert.close();
			}
			result.close();
			sql.close();
			manager.release("minehome", conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public static void delHome(UUID uuid, String home) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minehome");
			PreparedStatement sql = conn.prepareStatement("SELECT home_name FROM homes WHERE player = '"
					+ uuid.toString().replace("-", "") + "' AND home_name = '" + home + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				PreparedStatement update = conn.prepareStatement("DELETE FROM homes WHERE player = '"
						+ uuid.toString().replace("-", "") + "' AND home_name = '" + home + "';");
				update.executeUpdate();
				update.close();
			}
			result.close();
			sql.close();
			manager.release("minehome", conn);

		}

		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<String> getHome(UUID uuid, String home) {
		final List<String> rlist = new ArrayList<String>();

		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minehome");
			PreparedStatement sql = conn
					.prepareStatement("SELECT world, server, x, y, z, yaw, pitch FROM homes WHERE player = '"
							+ uuid.toString().replace("-", "") + "' AND home_name = '" + home + "';");
			final ResultSet result = sql.executeQuery();
			if (result.next()) {
				rlist.add(0, uuid.toString());
				rlist.add(1, result.getString(1));
				rlist.add(2, result.getString(2));
				rlist.add(3, result.getString(3));
				rlist.add(4, result.getString(4));
				rlist.add(5, result.getString(5));
				rlist.add(6, result.getString(6));
				rlist.add(7, result.getString(7));
				result.close();
				sql.close();
				manager.release("minehome", conn);
			} else {
				result.close();
				sql.close();
				manager.release("minehome", conn);
				return null;
			}
			return rlist;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static boolean isHome(UUID uuid, String home) {
		boolean ishome = false;
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {
			Connection conn = manager.getConnection("minehome");
			PreparedStatement sql = conn.prepareStatement("SELECT home_name FROM homes WHERE player = '"
					+ uuid.toString().replace("-", "") + "' AND home_name = '" + home + "';");
			ResultSet result = sql.executeQuery();
			if (result.next()) {
				ishome = true;
			} else {
				ishome = false;
			}
			result.close();
			sql.close();
			manager.release("minehome", conn);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ishome;
	}

	public static HashMap<String, String> getHomes(UUID uuid) {
		ConnectionManager manager = ConnectionManager.DEFAULT;
		try {

			Connection conn = manager.getConnection("minehome");
			PreparedStatement sel = conn
					.prepareStatement("SELECT * FROM homes WHERE player = '" + uuid.toString().replace("-", "") + "';");
			HashMap<String, String> list = new HashMap<String, String>();
			try {
				ResultSet result = sel.executeQuery();
				if (result != null) {
					@SuppressWarnings("unused")
					int i = 0;
					while (result.next()) {
						list.put(result.getString("home_name"), result.getString("server"));
						i++;
					}
				}
				result.close();
				sel.close();
				manager.release("minehome", conn);
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

}
