package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.utils.Permission;
import fr.maxlego08.text.command.VCommand;
import fr.maxlego08.text.zcore.utils.commands.CommandType;

public class CommandTextGenerator extends VCommand {

    public CommandTextGenerator(TextPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_USE);
        this.addSubCommand(new CommandTextGeneratorReload(plugin));
        this.addSubCommand(new CommandTextGeneratorBook(plugin));
    }

    @Override
    protected CommandType perform(TextPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }
}
