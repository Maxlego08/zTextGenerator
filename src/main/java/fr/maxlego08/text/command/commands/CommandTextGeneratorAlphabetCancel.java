package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandType;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class CommandTextGeneratorAlphabetCancel extends VCommand {

    public CommandTextGeneratorAlphabetCancel(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_TEST_ALPHABET);
        this.addSubCommand("cancel");
        this.addRequireArg("player", (sender, args) -> plugin.getServer().getOnlinePlayers().stream()
                .filter(online -> plugin.getTextManager().isAlphabetValidationRunning(online.getUniqueId()))
                .map(Player::getName)
                .collect(Collectors.toList()));
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {

        String playerName = this.argAsString(0);
        if (playerName == null || playerName.isEmpty()) {
            syntaxMessage();
            return CommandType.SUCCESS;
        }

        Player target = this.argAsPlayer(0);
        if (target == null) {
            message(plugin, sender, Message.TEXT_PLAYER_NOT_FOUND, "%player%", playerName);
            return CommandType.SUCCESS;
        }

        var manager = plugin.getTextManager();
        if (!manager.isAlphabetValidationRunning(target.getUniqueId())) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_NOT_RUNNING, "%player%", target.getName());
            return CommandType.SUCCESS;
        }

        boolean cancelled = manager.cancelAlphabetValidation(target.getUniqueId());
        if (!cancelled) {
            message(plugin, sender, Message.ALPHABET_VALIDATION_NOT_RUNNING, "%player%", target.getName());
            return CommandType.SUCCESS;
        }

        message(plugin, sender, Message.ALPHABET_VALIDATION_CANCELLED, "%player%", target.getName());
        message(plugin, target, Message.ALPHABET_VALIDATION_CANCELLED_TARGET);
        return CommandType.SUCCESS;
    }
}
