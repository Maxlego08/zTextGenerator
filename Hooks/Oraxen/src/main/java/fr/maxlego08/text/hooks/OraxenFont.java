package fr.maxlego08.text.hooks;

import fr.maxlego08.text.api.fonts.FontImage;
import io.th0rgal.oraxen.OraxenPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OraxenFont implements FontImage {

    private final Pattern pattern = Pattern.compile("<glyph:([^>]+)>");

    @Override
    public String replace(String string) {
        Matcher matcher = pattern.matcher(string);
        StringBuilder result = new StringBuilder();
        while (matcher.find()) {
            var value = OraxenPlugin.get().getFontManager().getGlyphFromID(matcher.group(0));
            if (value == null) continue;
            String replacement = value.getCharacter();
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        return result.toString().replace("§f", "").replace("§r", "");
    }
}
