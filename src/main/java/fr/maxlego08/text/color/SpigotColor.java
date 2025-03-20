package fr.maxlego08.text.color;

import fr.maxlego08.text.api.color.ColorHelper;

public class SpigotColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return text.replaceAll("(§[0-9A-FK-ORa-fk-or])", "");
    }
}
