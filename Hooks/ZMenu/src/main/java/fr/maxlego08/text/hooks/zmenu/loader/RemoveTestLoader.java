package fr.maxlego08.text.hooks.zmenu.loader;

import fr.maxlego08.menu.api.loader.ActionLoader;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.TypedMapAccessor;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.hooks.zmenu.actions.AddTestAction;
import fr.maxlego08.text.hooks.zmenu.actions.RemoveTestAction;

import java.io.File;

public class RemoveTestLoader extends ActionLoader {

    private final TextGeneratorPlugin plugin;

    public RemoveTestLoader(TextGeneratorPlugin plugin) {
        super("ztextgenerator-remove-value");
        this.plugin = plugin;
    }

    @Override
    public Action load(String path, TypedMapAccessor accessor, File file) {
        return new RemoveTestAction(this.plugin);
    }
}
