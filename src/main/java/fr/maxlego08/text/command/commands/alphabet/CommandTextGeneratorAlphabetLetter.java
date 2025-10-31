package fr.maxlego08.text.command.commands.alphabet;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandType;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;

import java.util.Arrays;
import java.util.List;

public class CommandTextGeneratorAlphabetLetter extends VCommand {

    public CommandTextGeneratorAlphabetLetter(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_TEST_ALPHABET);
        this.addSubCommand("letter");
        this.addRequireArg("letter", (sender, args) -> Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l",
                "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
        this.addRequireArg("alphabet", (sender, args) -> plugin.getTextManager().getAlphabets().stream().map(Alphabet::getName).toList());
        this.addOptionalArg("letter by line", (sender, args) -> Arrays.asList("10", "20", "30", "40", "50", "60", "70", "80", "90", "100"));
        this.addOptionalArg("max lines", (sender, args) -> Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"));
        this.addOptionalArg("letter length", (sender, args) -> {
            if (args.length <= 3) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
            }

            String letterArg = args[2];
            String alphabetName = args[3];
            if (letterArg.isEmpty()) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
            }

            var optional = plugin.getTextManager().getAlphabet(alphabetName);
            if (optional.isEmpty()) {
                return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
            }

            var alphabet = optional.get();
            return List.of(String.valueOf(alphabet.getLength(letterArg.charAt(0))));
        });
        this.addOptionalArg("inventory title", (sender, args) -> List.of(":offset_-48::generic_dark::offset_-168:"));
        this.addOptionalArg("inventory size", (sender, args) -> Arrays.asList("9", "18", "27", "36", "45", "53"));
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {

        String letter = this.argAsString(0);
        String alphabetName = this.argAsString(1);
        int letterByLine = this.argAsInteger(2, 10);
        int maxLines = this.argAsInteger(3, 6);

        if (maxLines < 1 || letterByLine < 1) return CommandType.SYNTAX_ERROR;

        var manager = plugin.getTextManager();
        var optional = manager.getAlphabet(alphabetName);
        if (optional.isEmpty()) {
            message(plugin, sender, Message.ALPHABET_NOT_FOUND, "%alphabet%", alphabetName);
            return CommandType.DEFAULT;
        }

        var alphabet = optional.get();
        int letterLength = this.argAsInteger(4, alphabet.getLength(letter.charAt(0)));
        String inventoryName = this.argAsString(5, ":offset_-48::generic_dark::offset_-168:");
        int inventorySize = this.argAsInteger(6, 54);

        manager.displayAlphabet(player, alphabet, letter, letterByLine, maxLines, letterLength, inventoryName, inventorySize);

        return CommandType.SUCCESS;
    }
}
