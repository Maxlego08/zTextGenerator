package fr.maxlego08.text.command.commands.alphabet;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandType;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;

public class CommandTextGeneratorAlphabet extends VCommand {

    public CommandTextGeneratorAlphabet(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_TEST_ALPHABET);
        this.addSubCommand("alphabet");
        this.setDescription(Message.DESCRIPTION_TEST_ALPHABET);
        this.addSubCommand(new CommandTextGeneratorAlphabetLetter(plugin));
        this.addSubCommand(new CommandTextGeneratorAlphabetValidation(plugin));
        this.addSubCommand(new CommandTextGeneratorAlphabetCancel(plugin));
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {
        syntaxMessage();
        return CommandType.SUCCESS;
    }
}
