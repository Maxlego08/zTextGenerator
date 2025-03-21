package fr.maxlego08.text.api;

import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.font.FontImage;
import fr.maxlego08.text.api.messages.MessageManager;
import org.bukkit.plugin.Plugin;

public interface TextGeneratorPlugin extends Plugin {

    TextManager getTextManager();

    ColorHelper getColorHelper();

    boolean isEnableDebug();

    void debug(String message);

    void reloadConfigurations();

    MessageManager getMessageManager();

    FontImage getFontImage();
}
