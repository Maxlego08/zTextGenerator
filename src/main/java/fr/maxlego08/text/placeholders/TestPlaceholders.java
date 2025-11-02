package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;

public class TestPlaceholders extends PlaceholderRegister {

    private final TextGeneratorPlugin plugin;

    public TestPlaceholders(TextGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(TextManager textManager) {

        register("test", (a, b) -> String.valueOf(this.plugin.getTestValue()), "Displays the example value for example purposes");
    }
}
