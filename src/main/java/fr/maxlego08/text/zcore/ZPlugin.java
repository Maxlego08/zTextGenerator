package fr.maxlego08.text.zcore;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.placeholders.LocalPlaceholder;
import fr.maxlego08.text.api.placeholders.Placeholder;
import fr.maxlego08.text.command.CommandManager;
import fr.maxlego08.text.command.VCommand;
import fr.maxlego08.text.messages.MessageLoader;
import fr.maxlego08.text.zcore.utils.documentations.CommandMarkdownGenerator;
import fr.maxlego08.text.zcore.utils.documentations.PlaceholderMarkdownGenerator;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public abstract class ZPlugin extends JavaPlugin implements TextGeneratorPlugin {

    protected final MessageLoader messageLoader = new MessageLoader(this);
    protected CommandManager commandManager;

    protected void preEnable() {

        LocalPlaceholder.getInstance().setPlugin(this);
        Placeholder.getPlaceholder();

        this.messageLoader.load();
    }

    protected void postEnable() {

        File folder = new File(getDataFolder(), "docs");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File filePlaceholder = new File(folder, "placeholders.md");
        File fileCommand = new File(folder, "commands.md");

        try {
            PlaceholderMarkdownGenerator placeholderMarkdownGenerator = new PlaceholderMarkdownGenerator();
            placeholderMarkdownGenerator.generateMarkdownFile(LocalPlaceholder.getInstance().getAutoPlaceholders(), filePlaceholder.toPath());
            getLogger().info("Markdown 'placeholders.md' file successfully generated!");

            CommandMarkdownGenerator commandMarkdownGenerator = new CommandMarkdownGenerator();
            commandMarkdownGenerator.generateMarkdownFile(this.commandManager.getCommands(), fileCommand.toPath());
            getLogger().info("Markdown 'commands.md' file successfully generated!");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.commandManager.validCommands();
    }

    protected void preDisable() {

    }

    protected void postDisable() {

    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    protected void registerCommand(String command, VCommand vCommand, String... aliases) {
        this.commandManager.registerCommand(this, command, vCommand, Arrays.asList(aliases));
    }
}
