package de.nlinz.xeonSuite.home.api;

import de.keks.socket.bukkit.BukkitPlugin;
import de.keks.socket.core.Channel;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class HOStreamOutApi {

	public static void sendTeleportToHomeOut(String player, String server, String world, Double x, Double y, Double z,
			Float yaw, Float pitch) {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		DataOutputStream out = Channel.homeChannel(b);
		try {
			out.writeUTF("TeleportToHome");
			out.writeUTF(player);
			out.writeUTF(server);
			out.writeUTF(world);
			out.writeDouble(x);
			out.writeDouble(y);
			out.writeDouble(z);
			out.writeFloat(yaw);
			out.writeFloat(pitch);
		} catch (IOException e) {
			e.printStackTrace();
		}

		BukkitPlugin.instance().sendBytesOut(b);
	}

}
