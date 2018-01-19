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

package de.linzn.mineSuite.home.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.database.HomeSqlActions;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class HomeListCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public HomeListCommand(HomePlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.homes")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    Player p = (Player) sender;

                    HashMap<String, String> list = HomeSqlActions.getHomes(p.getUniqueId());
                    List<String> homename = new ArrayList<String>();
                    List<String> servername = new ArrayList<String>();

                    for (Map.Entry<String, String> s : list.entrySet()) {
                        homename.add(s.getKey());
                        servername.add(s.getValue());
                    }

                    int pageNumb = 0;
                    try {
                        if (args.length == 1) {
                            int number = Integer.valueOf(args[0]);
                            if (number < 1) {
                                pageNumb = 0;
                            } else {
                                pageNumb = Integer.valueOf(args[0]) - 1;
                            }

                        } else {
                            pageNumb = 0;
                        }
                    } catch (Exception e) {
                        p.sendMessage("No number");
                        return;
                    }
                    int counter = 1;
                    int rgCount = list.size();
                    if ((pageNumb * 6 + 1) > rgCount) {
                        sender.sendMessage("So viele Homeseiten besitzt du nicht!");
                        return;
                    }
                    Collections.sort(homename);
                    sender.sendMessage("§aAuflistung all deiner Homes: ");
                    sender.sendMessage("§aServername?   §9Homename? ");
                    List<String> homelist = homename.subList(pageNumb * 6,
                            pageNumb * 6 + 6 > rgCount ? rgCount : pageNumb * 6 + 6);
                    for (String s : homelist) {
                        sender.sendMessage("§a" + list.get(s) + ": §9" + s);
                        counter++;
                    }
                    if (counter >= 7) {

                        int pageSeite;
                        if (pageNumb == 0) {
                            pageSeite = 2;
                        } else {
                            pageSeite = (pageNumb + 2);
                        }
                        sender.sendMessage("§aMehr auf Seite §e" + pageSeite + " §amit §e/homes " + pageSeite);
                    }

                    if (counter <= 6 && pageNumb != 0) {
                        int pageSeite = (pageNumb);

                        sender.sendMessage("§aZurück auf Seite §e" + pageSeite + "§a mit §e/homes " + pageSeite);
                    }

                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
