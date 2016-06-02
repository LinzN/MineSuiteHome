package de.nlinz.xeonSuite.home.listener;

import java.io.DataInputStream;
import java.io.IOException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import de.keks.socket.bukkit.events.plugin.BukkitSockHomeEvent;
import de.keks.socket.core.ByteStreamConverter;
import de.nlinz.xeonSuite.bukkit.XeonSuiteBukkit;
import de.nlinz.xeonSuite.home.api.HOStreamInApi;

public class BukkitSockHomeListener implements Listener {

	@EventHandler
	public void onBukkitSockBanEvent(final BukkitSockHomeEvent e) {

		DataInputStream in = ByteStreamConverter.toDataInputStream(e.readBytes());
		String task = null;
		String servername = null;
		try {
			servername = in.readUTF();

			if (!servername.equalsIgnoreCase(XeonSuiteBukkit.getServerName())) {
				return;
			}

			task = in.readUTF();

			if (task.equals("TeleportToHome")) {
				HOStreamInApi.teleportToHome(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(), in.readDouble(),
						in.readFloat(), in.readFloat());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}