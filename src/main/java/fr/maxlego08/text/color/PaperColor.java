package fr.maxlego08.text.color;

import fr.maxlego08.text.api.color.ColorHelper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.command.CommandSender;

public class PaperColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(text));
    }

    @Override
    public void message(CommandSender sender, String string) {

    }
}
