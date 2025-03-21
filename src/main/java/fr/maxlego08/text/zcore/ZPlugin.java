package fr.maxlego08.text.zcore;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.placeholders.LocalPlaceholder;
import fr.maxlego08.text.api.placeholders.Placeholder;
import fr.maxlego08.text.zcore.utils.PlaceholderMarkdownGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public abstract class ZPlugin extends JavaPlugin implements TextGeneratorPlugin {

    protected void preEnable() {

        LocalPlaceholder.getInstance().setPlugin(this);
        Placeholder.getPlaceholder();

    }

    protected void postEnable() {

        File filePlaceholder = new File(getDataFolder(), "placeholders.md");

        try {
            PlaceholderMarkdownGenerator placeholderMarkdownGenerator = new PlaceholderMarkdownGenerator();
            placeholderMarkdownGenerator.generateMarkdownFile(LocalPlaceholder.getInstance().getAutoPlaceholders(), filePlaceholder.toPath());
            getLogger().info("Markdown 'placeholders.md' file successfully generated!");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    protected void preDisable() {

    }

    protected void postDisable() {

    }

}
