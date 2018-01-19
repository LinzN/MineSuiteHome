package de.linzn.mineSuite.home.commands;

import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.core.database.hashDatabase.HomeDataTable;
import de.linzn.mineSuite.home.HomePlugin;
import de.linzn.mineSuite.home.database.HomeSqlActions;
import de.linzn.mineSuite.home.socket.JClientHomeOutput;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HomeCommand implements CommandExecutor {
    public ThreadPoolExecutor executorServiceCommands = new ThreadPoolExecutor(1, 1, 250L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    public HomeCommand(HomePlugin instance) {

    }

    @Override
    public boolean onCommand(final CommandSender sender, Command cmd, String label, final String[] args) {
        final Player player = (Player) sender;
        if (player.hasPermission("mineSuite.home.home")) {
            this.executorServiceCommands.submit(() -> {
                if (sender instanceof Player) {

                    String homeName = "home";
                    if ((args.length == 1)) {
                        homeName = args[0].toLowerCase();
                    }

                    if (HomeSqlActions.isHome(player.getUniqueId(), homeName)) {
                        if (!player.hasPermission("xeonSuite.bypass")) {
                            HomeDataTable.lastHomeLocation.put(player, player.getLocation());
                            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_TIMER.replace("{TIME}",
                                    String.valueOf(MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP)));
                            HomePlugin.inst().getServer().getScheduler().runTaskLater(HomePlugin.inst(),
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            String homelName = "home";
                                            if ((args.length == 1)) {
                                                homelName = args[0].toLowerCase();
                                            }
                                            Location loc = HomeDataTable.lastHomeLocation.get(player);
                                            HomeDataTable.lastHomeLocation.remove(player);
                                            if ((loc != null)
                                                    && (loc.getBlock().equals(player.getLocation().getBlock()))) {
                                                List<String> list = HomeSqlActions.getHome(player.getUniqueId(),
                                                        homelName);
                                                String world = list.get(1);
                                                String server = list.get(2);
                                                double x = Double.parseDouble(list.get(3));
                                                double y = Double.parseDouble(list.get(4));
                                                double z = Double.parseDouble(list.get(5));
                                                float yaw = Float.parseFloat(list.get(6));
                                                float pitch = Float.parseFloat(list.get(7));
                                                JClientHomeOutput.sendTeleportToHomeOut(player.getName(), server,
                                                        world, x, y, z, yaw, pitch);
                                                return;
                                            } else {
                                                player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.TELEPORT_MOVE_CANCEL);

                                            }
                                        }
                                    }, 20L * MineSuiteCorePlugin.getInstance().getMineConfigs().generalConfig.TELEPORT_WARMUP);
                        } else {
                            List<String> list = HomeSqlActions.getHome(player.getUniqueId(), homeName);
                            String world = list.get(1);
                            String server = list.get(2);
                            double x = Double.parseDouble(list.get(3));
                            double y = Double.parseDouble(list.get(4));
                            double z = Double.parseDouble(list.get(5));
                            float yaw = Float.parseFloat(list.get(6));
                            float pitch = Float.parseFloat(list.get(7));
                            JClientHomeOutput.sendTeleportToHomeOut(player.getName(), server, world, x, y, z, yaw,
                                    pitch);
                            return;

                        }

                    } else {
                        player.sendMessage(
                                ChatColor.GOLD + "Du besitzt keine Homes oder dieses Home existiert nicht!");
                    }
                }
            });
        } else {
            player.sendMessage(MineSuiteCorePlugin.getInstance().getMineConfigs().generalLanguage.NO_PERMISSIONS);
        }
        return false;
    }
}
