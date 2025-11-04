package fr.maxlego08.text.hooks;

import fr.maxlego08.text.api.fonts.FontImage;
import io.th0rgal.oraxen.OraxenPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OraxenFont implements FontImage {

    private final Pattern pattern = Pattern.compile("<glyph_(\\w+)>");

    @Override
    public String replace(String string, boolean removeColor) {
        var manager = OraxenPlugin.get().getFontManager();
        Matcher matcher = pattern.matcher(string);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            var value = manager.getGlyphFromID(matcher.group(1));
            if (value == null) continue;
            String replacement = value.getCharacter();
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return removeColor ? result.toString().replace("§f", "").replace("§r", "") : result.toString();
    }
}
