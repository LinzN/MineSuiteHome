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

package de.linzn.mineSuite.home;


import de.linzn.mineSuite.home.commands.DeleteHomeCommand;
import de.linzn.mineSuite.home.commands.HomeCommand;
import de.linzn.mineSuite.home.commands.HomeListCommand;
import de.linzn.mineSuite.home.commands.SetHomeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class HomePlugin extends JavaPlugin {
    private static HomePlugin inst;

    public static HomePlugin inst() {
        return inst;
    }

    @Override
    public void onEnable() {
        inst = this;
        loadCommands();
    }

    @Override
    public void onDisable() {
    }

    private void loadCommands() {
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("homes").setExecutor(new HomeListCommand());
        getCommand("delhome").setExecutor(new DeleteHomeCommand());
    }

}
