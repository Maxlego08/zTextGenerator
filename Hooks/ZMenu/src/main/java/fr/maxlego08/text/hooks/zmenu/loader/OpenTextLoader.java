package fr.maxlego08.text.hooks.zmenu.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.text.animation.TextAnimationType;
import fr.maxlego08.text.hooks.zmenu.actions.OpenTextAction;

import java.io.File;

public class OpenTextLoader extends ActionLoader {

    private final TextGeneratorPlugin plugin;

    public OpenTextLoader(TextGeneratorPlugin plugin) {
        super("text open");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {

        String text = accessor.getString("text");
        TextAnimationType textAnimationType = accessor.contains("animation") ? TextAnimationType.valueOf(accessor.getString("animation")) : TextAnimationType.NONE;
        long delay = accessor.contains("delay") ? accessor.getLong("delay") : 100;
        if (delay <= 0) {
            this.plugin.getLogger().warning("Delay must be greater than 0 for text open action, path: " + path + ", file: " + file.getPath());
            return null;
        }

        return new OpenTextAction(this.plugin, text, textAnimationType, delay);
    }
}
