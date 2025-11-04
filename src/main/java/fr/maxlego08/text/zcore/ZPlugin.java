package fr.maxlego08.text.zcore;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandManager;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.placeholders.LocalPlaceholder;
import fr.maxlego08.text.api.placeholders.Placeholder;
import fr.maxlego08.text.api.utils.Plugins;
import fr.maxlego08.text.command.ZCommandManager;
import fr.maxlego08.text.messages.MessageLoader;
import fr.maxlego08.text.zcore.utils.documentations.CommandMarkdownGenerator;
import fr.maxlego08.text.zcore.utils.documentations.PlaceholderMarkdownGenerator;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Optional;

public abstract class ZPlugin extends JavaPlugin implements TextGeneratorPlugin {

    protected final MessageLoader messageLoader = new MessageLoader(this);
    protected ZCommandManager ZCommandManager;

    protected void preEnable() {

        var folder = getDataFolder();
        if (!folder.exists()) {
            folder.mkdirs();
        }

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
            commandMarkdownGenerator.generateMarkdownFile(this.ZCommandManager.getCommands(), fileCommand.toPath());
            getLogger().info("Markdown 'commands.md' file successfully generated!");
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.ZCommandManager.validCommands();
    }

    protected void preDisable() {

    }

    protected void postDisable() {

    }

    @Override
    public CommandManager getCommandManager() {
        return ZCommandManager;
    }

    public void registerCommand(String command, VCommand vCommand, String... aliases) {
        this.ZCommandManager.registerCommand(this, command, vCommand, Arrays.asList(aliases));
    }

    protected void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    protected boolean isActive(Plugins plugins) {
        return getServer().getPluginManager().getPlugin(plugins.getName()) != null;
    }

    protected <T> Optional<T> createInstance(String className) {
        try {
            Class<?> clazz = Class.forName("fr.maxlego08.text.hooks." + className);

            try {
                Constructor<?> constructor = clazz.getConstructor(TextGeneratorPlugin.class);
                // noinspection unchecked
                return Optional.of((T) constructor.newInstance(this));
            } catch (NoSuchMethodException error) {
                // noinspection unchecked
                return Optional.of((T) clazz.newInstance());
            }
        } catch (ClassNotFoundException ignored) {
            return Optional.empty();
        } catch (Exception error) {
            error.printStackTrace();
            return Optional.empty();
        }
    }

    public <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
        if (provider == null) {
            getLogger().severe("Unable to retrieve the provider " + classz);
            return null;
        }
        return provider.getProvider() != null ? provider.getProvider() : null;
    }
}
