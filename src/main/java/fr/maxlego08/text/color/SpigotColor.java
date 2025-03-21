package fr.maxlego08.text.color;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;
import org.bukkit.command.CommandSender;

public class SpigotColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return text.replaceAll("(§[0-9A-FK-ORa-fk-or])", "");
    }

    @Override
    public Result transformString(Alphabet alphabet, String string, int height) {
        StringBuilder builder = new StringBuilder();
        int length = 0;
        for (char c : string.toCharArray()) {
            builder.append(alphabet.transformChar(c, height));
            length += alphabet.getLength(c);
        }
        return new Result(builder.toString(), length);
    }

    @Override
    public void message(CommandSender sender, String string) {
        sender.sendMessage(string.replace("&", "§"));
    }
}
