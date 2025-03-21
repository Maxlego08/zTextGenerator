package fr.maxlego08.text;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.color.PaperColor;
import fr.maxlego08.text.placeholders.AlignedPlaceholders;
import fr.maxlego08.text.zcore.ZPlugin;

import java.util.List;

public final class TextPlugin extends ZPlugin {

    private final TextManager textManager = new ZTextManager(this);
    private final ColorHelper colorHelper = new PaperColor();

    @Override
    public void onEnable() {
        preEnable();

        this.textManager.loadAlphabets();

        var placeholders = List.of(new AlignedPlaceholders());
        placeholders.forEach(placeholder -> placeholder.register(this.textManager));

        postEnable();
    }

    @Override
    public void onDisable() {
        preDisable();


        postDisable();
    }

    @Override
    public TextManager getTextManager() {
        return textManager;
    }

    @Override
    public ColorHelper getColorHelper() {
        return colorHelper;
    }
}
