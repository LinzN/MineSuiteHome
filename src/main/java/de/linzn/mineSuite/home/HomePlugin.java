package de.linzn.mineSuite.home;


import de.linzn.mineSuite.core.MineSuiteCorePlugin;
import de.linzn.mineSuite.home.commands.DeleteHomeCommand;
import de.linzn.mineSuite.home.commands.HomeCommand;
import de.linzn.mineSuite.home.commands.HomeListCommand;
import de.linzn.mineSuite.home.commands.SetHomeCommand;
import de.linzn.mineSuite.home.listener.HomeListener;
import de.linzn.mineSuite.home.socket.JClientHomeListener;
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
        MineSuiteCorePlugin.getInstance().getMineJSocketClient().jClientConnection1.registerIncomingDataListener("mineSuiteHome", new JClientHomeListener());
        getServer().getPluginManager().registerEvents(new HomeListener(), this);
    }

    @Override
    public void onDisable() {
    }

    public void loadCommands() {
        getCommand("home").setExecutor(new HomeCommand(this));
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("homes").setExecutor(new HomeListCommand(this));
        getCommand("delhome").setExecutor(new DeleteHomeCommand(this));
    }

}
