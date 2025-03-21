package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;
import fr.maxlego08.text.command.VCommand;
import fr.maxlego08.text.zcore.utils.commands.CommandType;

public class CommandTextGeneratorReload extends VCommand {

    public CommandTextGeneratorReload(TextPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_RELOAD);
        this.addSubCommand("reload", "rl");
        this.setDescription(Message.DESCRIPTION_RELOAD);
    }

    @Override
    protected CommandType perform(TextPlugin plugin) {
        plugin.reloadConfigurations();
        message(plugin, sender, Message.RELOAD);
        return CommandType.SUCCESS;
    }
}
