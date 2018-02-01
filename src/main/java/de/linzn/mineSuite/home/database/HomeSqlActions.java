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

package de.linzn.mineSuite.home.database;

import de.linzn.mineSuite.core.database.mysql.MySQLConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class HomeSqlActions {

    public static HashMap<String, String> getHomes(UUID uuid) {
        MySQLConnectionManager manager = MySQLConnectionManager.DEFAULT;
        try {

            Connection conn = manager.getConnection("MineSuiteHome");
            PreparedStatement sel = conn
                    .prepareStatement("SELECT * FROM module_home_homes WHERE player = '" + uuid.toString().replace("-", "") + "';");
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
                    result.close();
                }
                sel.close();
                manager.release("MineSuiteHome", conn);
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
