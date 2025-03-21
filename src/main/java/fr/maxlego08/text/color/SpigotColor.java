package fr.maxlego08.text.color;

import fr.maxlego08.text.api.color.ColorHelper;
import org.bukkit.command.CommandSender;

public class SpigotColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return text.replaceAll("(§[0-9A-FK-ORa-fk-or])", "");
    }

    @Override
    public void message(CommandSender sender, String string) {
        sender.sendMessage(string.replace("&", "§"));
    }
}
