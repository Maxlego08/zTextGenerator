package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandType;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;

import java.util.Arrays;

public class CommandTextGeneratorAlphabetValidation extends VCommand {

    public CommandTextGeneratorAlphabetValidation(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_TEST_ALPHABET);
        this.addSubCommand("validation");
        this.addRequireArg("alphabet", (sender, args) -> plugin.getTextManager().getAlphabets().stream().map(Alphabet::getName).toList());
        this.addOptionalArg("delay", (sender, args) -> Arrays.asList("1", "2", "3", "5", "10", "15", "20", "30"));
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {

        String alphabetName = this.argAsString(0);
        if (alphabetName == null || alphabetName.isEmpty()) {
            syntaxMessage();
            return CommandType.SUCCESS;
        }

        var optionalAlphabet = plugin.getTextManager().getAlphabet(alphabetName);
        if (optionalAlphabet.isEmpty()) {
            message(plugin, sender, Message.ALPHABET_NOT_FOUND, "%alphabet%", alphabetName);
            return CommandType.DEFAULT;
        }

        int delaySeconds;
        try {
            delaySeconds = this.argAsInteger(1, 2);
        } catch (Exception exception) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_DELAY_INVALID);
            return CommandType.SUCCESS;
        }

        if (delaySeconds < 1) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_DELAY_INVALID);
            return CommandType.SUCCESS;
        }

        var manager = plugin.getTextManager();
        if (manager.isAlphabetValidationRunning(player.getUniqueId())) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_ALREADY_RUNNING);
            return CommandType.SUCCESS;
        }

        var alphabet = optionalAlphabet.get();
        if (alphabet.getFontInfos().isEmpty()) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_EMPTY);
            return CommandType.SUCCESS;
        }

        boolean started = manager.startAlphabetValidation(player, alphabet, delaySeconds);
        if (!started) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_ALREADY_RUNNING);
            return CommandType.SUCCESS;
        }

        message(plugin, sender, Message.ALPHABET_VALIDATION_STARTED, "%alphabet%", alphabetName, "%delay%", String.valueOf(delaySeconds));
        return CommandType.SUCCESS;
    }
}
