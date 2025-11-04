package fr.maxlego08.text.api.utils;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.placeholders.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.stream.Stream;

public abstract class ZUtils {

    /**
     * Recursively iterates through all files in the given folder, skipping the
     * folder itself, and applies the given consumer to each file that ends with
     * ".yml".
     *
     * @param folder   the folder to iterate through
     * @param consumer the consumer to apply to each file
     */
    protected void files(File folder, Consumer<File> consumer) {
        try (Stream<Path> s = Files.walk(Paths.get(folder.getPath()))) {
            s.skip(1).map(Path::toFile).filter(File::isFile).filter(e -> e.getName().endsWith(".yml")).forEach(consumer);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Applies PlaceholderAPI transformations to a string.
     *
     * @param placeHolder the string to transform.
     * @param player      the player context for the placeholders.
     * @return the transformed string.
     */
    protected String papi(String placeHolder, Player player) {
        return Placeholder.getPlaceholder().setPlaceholders(player, placeHolder);
    }

    /**
     * Applies PlaceholderAPI transformations to a list of strings.
     *
     * @param placeHolder the list of strings to transform.
     * @param player      the player context for the placeholders.
     * @return the transformed list of strings.
     */
    protected List<String> papi(List<String> placeHolder, Player player) {
        return Placeholder.getPlaceholder().setPlaceholders(player, placeHolder);
    }

    protected void message(TextGeneratorPlugin plugin, CommandSender sender, Message message, Object... args) {
        plugin.getMessageManager().message(sender, message, args);
    }

    protected String parseContent(String content, Object... arguments) {

        if (arguments.length % 2 != 0)
            throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");

        for (int i = 0; i < arguments.length; i += 2) {
            if (arguments[i] == null || arguments[i + 1] == null)
                throw new IllegalArgumentException("Keys or replacement values must not be null.");
            content = content.replace(arguments[i].toString(), arguments[i + 1].toString());
        }
        return content;
    }

    protected boolean containsPlaceholder(String element) {
        return element != null && element.contains("%");
    }


    /**
     * Saves an embedded resource to a file on the disk.
     *
     * @param plugin       the plugin that contains the resource
     * @param resourcePath the path to the resource within the plugin's jar file
     * @param toPath       the path to save the resource to
     * @param replace      whether to overwrite the file if it already exists
     * @throws IllegalArgumentException if resourcePath is null or empty
     * @throws IllegalArgumentException if the embedded resource cannot be found
     */
    public void saveResource(Plugin plugin, String resourcePath, String toPath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = plugin.getResource(resourcePath);
            if (in == null) {
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + new File(plugin.getDataFolder(), "config.yml"));
            } else {
                File outFile = new File(plugin.getDataFolder(), toPath);
                int lastIndex = toPath.lastIndexOf(47);
                File outDir = new File(plugin.getDataFolder(), toPath.substring(0, lastIndex >= 0 ? lastIndex : 0));
                if (!outDir.exists()) {
                    outDir.mkdirs();
                }

                try {
                    if (outFile.exists() && !replace) {
                        plugin.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                    } else {
                        OutputStream out = Files.newOutputStream(outFile.toPath());
                        byte[] buf = new byte[1024];

                        int len;
                        while ((len = in.read(buf)) > 0) {
                            out.write(buf, 0, len);
                        }

                        out.close();
                        in.close();
                    }
                } catch (IOException var10) {
                    plugin.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
                }

            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

}
