package fr.maxlego08.text;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.text.command.ZTextCommand;

public final class ZTextPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommands();

    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
    }

    private void registerCommands() {
        ZTextCommand command = new ZTextCommand(this);
        PluginCommand pluginCommand = getCommand("ztext");
        if (pluginCommand == null) {
            getLogger().severe("Impossible d'enregistrer la commande ztext");
            return;
        }
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }
}
