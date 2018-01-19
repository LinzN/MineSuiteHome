package de.linzn.mineSuite.home.socket;

import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.home.api.HomeManager;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

public class JClientHomeListener implements IncomingDataListener {


    @Override
    public void onEvent(String channel, UUID clientUUID, byte[] dataInBytes) {
        // TODO Auto-generated method stub
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(dataInBytes));
        String subChannel;
        try {
            String serverName = in.readUTF();

            if (!serverName.equalsIgnoreCase(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME)) {
                return;
            }
            subChannel = in.readUTF();
            if (subChannel.equals("server_home_teleport-home")) {
                HomeManager.teleportToHome(in.readUTF(), in.readUTF(), in.readDouble(), in.readDouble(),
                        in.readDouble(), in.readFloat(), in.readFloat());
            }

        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

}
