package fr.maxlego08.text.hooks.zmenu.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.hooks.zmenu.actions.OpenBookAction;

import java.io.File;

public class OpenBookLoader extends ActionLoader {

    private final TextGeneratorPlugin plugin;

    public OpenBookLoader(TextGeneratorPlugin plugin) {
        super("book open");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {

        String book = accessor.getString("book");
        int page = accessor.getInt("page", 1);

        return new OpenBookAction(this.plugin, book, page);
    }
}
