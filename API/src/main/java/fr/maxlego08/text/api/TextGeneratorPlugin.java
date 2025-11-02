package fr.maxlego08.text.api;

import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.commands.CommandManager;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.fonts.FontImage;
import fr.maxlego08.text.api.messages.MessageManager;
import org.bukkit.plugin.Plugin;

public interface TextGeneratorPlugin extends Plugin {

    /**
     * Retrieves the TextManager associated with this plugin.
     *
     * @return the TextManager associated with this plugin.
     */
    TextManager getTextManager();

    /**
     * Retrieves the ColorHelper associated with this plugin.
     *
     * @return the ColorHelper associated with this plugin.
     */
    ColorHelper getColorHelper();

    /**
     * Retrieves whether debug mode is enabled for this plugin.
     *
     * @return true if debug mode is enabled, false otherwise
     */
    boolean isEnableDebug();

    /**
     * Logs a debug message in the console if debug mode is enabled.
     *
     * @param message the message to log.
     */
    void debug(String message);

    /**
     * Reloads the configuration files associated with this plugin.
     * This method is typically called when the plugin detects that its configuration files have changed.
     */
    void reloadConfigurations();

    /**
     * Retrieves the MessageManager associated with this plugin.
     * The MessageManager is responsible for sending messages to players,
     * and is used to send messages of various types.
     *
     * @return the MessageManager associated with this plugin
     */
    MessageManager getMessageManager();

    /**
     * Retrieves the FontImage associated with this plugin.
     * The FontImage is used to render text as an image.
     *
     * @return the FontImage associated with this plugin
     */
    FontImage getFontImage();

    /**
     * Retrieves the CommandManager associated with this plugin.
     * The CommandManager is responsible for registering and managing commands.
     *
     * @return the CommandManager associated with this plugin
     */
    CommandManager getCommandManager();

    /**
     * Registers a command with this plugin.
     *
     * @param command  the name of the command to register
     * @param vCommand the VCommand instance to register with the command
     * @param aliases  the aliases of the command to register
     */
    void registerCommand(String command, VCommand vCommand, String... aliases);

    <T> T getProvider(Class<T> classz);

    /**
     * Gets the default language configured for the plugin.
     *
     * @return the default language code, expressed using lowercase language and country codes (for example {@code en_us})
     */
    String getDefaultLanguage();

    int getTestValue();

    void setTestValue(int value);
}
