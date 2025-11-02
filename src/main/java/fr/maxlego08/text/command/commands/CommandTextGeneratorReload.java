package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.commands.CommandType;

public class CommandTextGeneratorReload extends VCommand {

    public CommandTextGeneratorReload(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_RELOAD);
        this.addSubCommand("reload", "rl");
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {
        plugin.reloadConfigurations();
        message(plugin, sender, Message.RELOAD);
        return CommandType.SUCCESS;
    }
}
