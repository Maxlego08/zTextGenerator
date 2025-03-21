package fr.maxlego08.text.api.color;

import org.bukkit.command.CommandSender;

public interface ColorHelper {

    String getTextWithoutColor(String text);

    void message(CommandSender sender, String string);
}
