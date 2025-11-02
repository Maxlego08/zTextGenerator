package fr.maxlego08.text.hooks.zmenu.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.hooks.zmenu.actions.AddTestAction;

import java.io.File;

public class AddTestLoader extends ActionLoader {

    private final TextGeneratorPlugin plugin;

    public AddTestLoader(TextGeneratorPlugin plugin) {
        super("ztextgenerator-add-value");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        return new AddTestAction(this.plugin);
    }
}
