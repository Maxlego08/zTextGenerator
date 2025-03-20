package fr.maxlego08.text.color;

import fr.maxlego08.text.api.color.ColorHelper;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class PaperColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return PlainTextComponentSerializer.plainText().serialize(MiniMessage.miniMessage().deserialize(text));
    }
}
