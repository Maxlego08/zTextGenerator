package fr.maxlego08.text.api.messages;

import org.bukkit.command.CommandSender;

public interface MessageManager {

    /**
     * Sends a message to a sender.
     *
     * @param sender the command sender.
     * @param message the message to send.
     * @param strings the strings to replace in the message.
     */
    void message(CommandSender sender, Message message, Object... strings);

}
