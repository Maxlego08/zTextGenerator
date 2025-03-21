package fr.maxlego08.text.api.color;

import fr.maxlego08.text.api.Alphabet;
import org.bukkit.command.CommandSender;

public interface ColorHelper {

    String getTextWithoutColor(String text);

    Result transformString(Alphabet alphabet, String string, int height);

    void message(CommandSender sender, String string);
}
