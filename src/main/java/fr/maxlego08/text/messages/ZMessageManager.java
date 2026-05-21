package fr.maxlego08.text.messages;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.messages.MessageManager;
import fr.maxlego08.text.api.messages.MessageType;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ZMessageManager extends ZUtils implements MessageManager {

    private final TextPlugin plugin;

    public ZMessageManager(TextPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void message(CommandSender sender, Message message, Object... objects) {

        if (sender instanceof Player player) {

            switch (message.getType()) {
                case ACTION -> {
                    this.plugin.getLogger().warning("MessageType ACTION is not yet implemented.");
                }
                case CENTER -> {
                    this.plugin.getLogger().warning("MessageType CENTER is not yet implemented.");
                }
                case TCHAT_AND_ACTION -> {
                    this.plugin.getLogger().warning("MessageType TCHAT_AND_ACTION is not yet implemented.");
                }
                case WITHOUT_PREFIX, TCHAT -> this.sendTchatMessage(player, message, objects);
            }

        } else {
            message.getMessages().forEach(str -> this.plugin.getColorHelper().message(sender, getMessage(str, objects)));
        }
    }

    private void sendTchatMessage(Player player, Message message, Object... args) {
        boolean isWithoutPrefix = message.getType() == MessageType.WITHOUT_PREFIX;
        List<String> messages = message.getMessages();
        for (int i = 0; i < messages.size(); i++) {
            String prefix = (isWithoutPrefix || i > 0) ? "" : Message.PREFIX.getMessage();
            this.plugin.getColorHelper().message(player, this.papi(prefix + getMessage(messages.get(i), args), player));
        }
    }

    private String getMessage(Message message, Object... args) {
        return getMessage(String.join("\n", message.getMessage()), args);
    }

    private String getMessage(String message, Object... arguments) {

        List<Object> modifiedArgs = new ArrayList<>();
        for (Object arg : arguments) handleArg(arg, modifiedArgs);
        Object[] newArgs = modifiedArgs.toArray();

        return getString(message, newArgs);
    }

    private void handleArg(Object arg, List<Object> modifiedArgs) {
        if (arg instanceof Player player) {
            addPlayerDetails(modifiedArgs, player.getName(), player.getDisplayName());
        } else {
            modifiedArgs.add(arg);
        }
    }

    private void addPlayerDetails(List<Object> modifiedArgs, String name, String displayName) {
        modifiedArgs.add("%player%");
        modifiedArgs.add(name);
        modifiedArgs.add("%displayName%");
        modifiedArgs.add(displayName);
    }

    private String getString(String message, Object[] newArgs) {
        if (newArgs.length % 2 != 0) {
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");
        }

        for (int i = 0; i < newArgs.length; i += 2) {
            if (newArgs[i] == null || newArgs[i + 1] == null) {
                throw new IllegalArgumentException("Keys and replacement values must not be null.");
            }
            message = message.replace(newArgs[i].toString(), newArgs[i + 1].toString());
        }
        return message;
    }
}
