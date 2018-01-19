package de.linzn.mineSuite.home.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.database.HomeSqlActions;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class SetHomeCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public SetHomeCommand(HomePlugin instance) {
    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmnd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.sethome")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {
                    int limit = 1;
                    if (player.hasPermission("mineSuite.home.limit.admin")) {
                        limit = 90000;
                    } else if (player.hasPermission("mineSuite.home.limit.9")) {
                        limit = 9;
                    } else if (player.hasPermission("mineSuite.home.limit.8")) {
                        limit = 8;
                    } else if (player.hasPermission("mineSuite.home.limit.7")) {
                        limit = 7;
                    } else if (player.hasPermission("mineSuite.home.limit.6")) {
                        limit = 6;
                    } else if (player.hasPermission("mineSuite.home.limit.5")) {
                        limit = 5;
                    } else if (player.hasPermission("mineSuite.home.limit.4")) {
                        limit = 4;
                    } else if (player.hasPermission("mineSuite.home.limit.3")) {
                        limit = 3;
                    } else if (player.hasPermission("mineSuite.home.limit.2")) {
                        limit = 2;
                    }
                    String homeName = "home";
                    Location coords = player.getLocation();
                    String server = MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.BUNGEE_SERVER_NAME;
                    String world = coords.getWorld().getName();
                    Double x = coords.getX();
                    Double y = coords.getY();
                    Double z = coords.getZ();
                    Float yaw = coords.getYaw();
                    Float pitch = coords.getPitch();

                    if (args.length == 1) {
                        homeName = args[0].toLowerCase();
                    }
                    if (!HomeSqlActions.isHome(player.getUniqueId(), homeName)) {
                        if (HomeSqlActions.getHomes(player.getUniqueId()).size() >= limit) {
                            sender.sendMessage(ChatColor.RED + "Du kannst nicht mehr als " + ChatColor.GOLD + limit
                                    + ChatColor.RED + " Homes setzen!");
                            return;
                        }
                    }
                    HomeSqlActions.setHome(player.getUniqueId(), homeName, server, world, x, y, z, yaw, pitch);
                    sender.sendMessage(ChatColor.GREEN + "Du hast das Home " + ChatColor.YELLOW + homeName
                            + ChatColor.GREEN + " gesetzt!");

                    return;
                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
