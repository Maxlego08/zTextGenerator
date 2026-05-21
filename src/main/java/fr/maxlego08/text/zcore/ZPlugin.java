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
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;

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

    /**
     * Registers a listener to this plugin, allowing it to receive events.
     *
     * @param listener the listener to register
     */
    protected void registerListener(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }

    /**
     * Checks if the given plugin is currently active.
     *
     * @param plugins the plugin to check
     * @return true if the plugin is active, false otherwise
     */
    protected boolean isActive(Plugins plugins) {
        return getServer().getPluginManager().getPlugin(plugins.getName()) != null;
    }

    /**
     * Attempts to create an instance of the class with the given name.
     * The class must be located in the "fr.maxlego08.text.hooks" package.
     *
     * If the class has a constructor that takes a single TextGeneratorPlugin argument,
     * an instance of the class will be created with the current plugin as its argument.
     * If the class does not have such a constructor, an instance of the class will be created
     * with no arguments.
     *
     * If the class cannot be found, or if an exception occurs while creating an instance,
     * an empty Optional will be returned.
     *
     * @param className the name of the class to create an instance of
     * @return an Optional containing the created instance, or an empty Optional if the class could not be found or an exception occurred
     */
    protected <T> Optional<T> createInstance(String className) {
        try {
            Class<?> clazz = Class.forName("fr.maxlego08.text.hooks." + className);

            try {
                Constructor<?> constructor = clazz.getConstructor(TextGeneratorPlugin.class);
                // noinspection unchecked
                return Optional.of((T) constructor.newInstance(this));
            } catch (NoSuchMethodException error) {
                // noinspection unchecked
                return Optional.of((T) clazz.getDeclaredConstructor().newInstance());
            }
        } catch (ClassNotFoundException ignored) {
            return Optional.empty();
        } catch (Exception error) {
            error.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieves a service provider for the given class.
     *
     * @param <T> the type of the service provider
     * @param classz the class of the service provider
     * @return the service provider, or null if it could not be found
     */
    public <T> T getProvider(Class<T> classz) {
        RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
        if (provider == null) {
            getLogger().severe("Unable to retrieve the provider " + classz);
            return null;
        }
        return provider.getProvider() != null ? provider.getProvider() : null;
    }
}
