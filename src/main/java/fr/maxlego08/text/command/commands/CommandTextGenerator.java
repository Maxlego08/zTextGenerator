package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.utils.Permission;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.commands.CommandType;

public class CommandTextGenerator extends VCommand {

    public CommandTextGenerator(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_USE);
        this.addSubCommand(new CommandTextGeneratorReload(plugin));
        this.addSubCommand(new CommandTextGeneratorBook(plugin));
        this.addSubCommand(new CommandTextGeneratorText(plugin));
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }
}
