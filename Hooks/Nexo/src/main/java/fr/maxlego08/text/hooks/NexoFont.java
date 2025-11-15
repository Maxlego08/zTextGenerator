package fr.maxlego08.text.hooks;

import fr.maxlego08.text.api.fonts.FontImage;

import java.util.regex.Pattern;

public class NexoFont implements FontImage {

    private final Pattern pattern = Pattern.compile("<glyph:(\\w+)>");

    @Override
    public String replace(String string, boolean removeColor) {

        /*Matcher matcher = pattern.matcher(string);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            var value = NexoPlugin.instance().fontManager().glyphFromID(matcher.group(1));
            if (value == null) continue;
            var replacement = value.getFormattedUnicodes();
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return removeColor ? result.toString().replace("§f", "").replace("§r", "") : result.toString();*/
        return string;
    }
}
