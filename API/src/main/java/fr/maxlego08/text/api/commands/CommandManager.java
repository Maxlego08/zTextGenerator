package fr.maxlego08.text.api.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.List;

public interface CommandManager extends CommandExecutor, TabCompleter {

    void validCommands();

    VCommand registerCommand(VCommand command);

    VCommand registerCommand(String string, VCommand command);

    void registerCommand(Plugin plugin, String string, VCommand vCommand, List<String> aliases);
}
