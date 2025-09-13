package fr.maxlego08.text.hooks;

import com.nexomc.nexo.NexoPlugin;
import fr.maxlego08.text.api.fonts.FontImage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NexoFont implements FontImage {

    private final Pattern pattern = Pattern.compile("<glyph:([^>]+)>");

    @Override
    public String replace(String string) {
        Matcher matcher = pattern.matcher(string);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            var value = NexoPlugin.instance().fontManager().glyphFromID(matcher.group(0));
            if (value == null) continue;
            String replacement = value.character();
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString();
    }
}
