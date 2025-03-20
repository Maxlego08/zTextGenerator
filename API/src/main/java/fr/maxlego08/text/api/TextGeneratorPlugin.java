package fr.maxlego08.text.api;

import fr.maxlego08.text.api.color.ColorHelper;
import org.bukkit.plugin.Plugin;

public interface TextGeneratorPlugin extends Plugin {

    TextManager getTextManager();

    ColorHelper getColorHelper();

}
