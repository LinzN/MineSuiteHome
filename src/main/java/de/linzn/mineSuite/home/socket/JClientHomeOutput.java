/*
 * Copyright (C) 2018. MineGaming - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 *  You should have received a copy of the LGPLv3 license with
 *  this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.mineSuite.home.socket;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import org.bukkit.Location;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

public class JClientHomeOutput {

    public static void sendTeleportToHomeOut(UUID playerUUID, String homeName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_home_teleport-home");
            dataOutputStream.writeUTF(homeName);
            dataOutputStream.writeUTF(playerUUID.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteHome", byteArrayOutputStream.toByteArray());

    }

    public static void sendHomeCreate(UUID playerUUID, String homeName, Location location, int homeLimit) {
        String server = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
        String world = location.getWorld().getName();
        Double x = location.getX();
        Double y = location.getY();
        Double z = location.getZ();
        Float yaw = location.getYaw();
        Float pitch = location.getPitch();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_home_create-home");
            dataOutputStream.writeUTF(homeName);
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeUTF(server);
            dataOutputStream.writeUTF(world);
            dataOutputStream.writeDouble(x);
            dataOutputStream.writeDouble(y);
            dataOutputStream.writeDouble(z);
            dataOutputStream.writeFloat(yaw);
            dataOutputStream.writeFloat(pitch);
            dataOutputStream.writeInt(homeLimit);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteHome", byteArrayOutputStream.toByteArray());

    }

    public static void sendHomeRemove(UUID playerUUID, String homeName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_home_remove-home");
            dataOutputStream.writeUTF(homeName);
            dataOutputStream.writeUTF(playerUUID.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteHome", byteArrayOutputStream.toByteArray());

    }

    public static void sendGetHomesList(UUID playerUUID, int pageid) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try {
            dataOutputStream.writeUTF("client_home_get-homes");
            dataOutputStream.writeUTF(playerUUID.toString());
            dataOutputStream.writeInt(pageid);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.writeOutput("mineSuiteHome", byteArrayOutputStream.toByteArray());

    }


}
