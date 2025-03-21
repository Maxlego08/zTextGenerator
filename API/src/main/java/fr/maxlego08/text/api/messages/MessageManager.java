package fr.maxlego08.text.api.messages;

import org.bukkit.command.CommandSender;

public interface MessageManager {

    void message(CommandSender sender, Message message, Object... strings);

}
