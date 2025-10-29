package fr.maxlego08.text.api.utils;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.placeholders.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.Consumer;
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

        if (arguments.length % 2 != 0) throw new IllegalArgumentException("Number of invalid arguments. Arguments must be in pairs.");

        for (int i = 0; i < arguments.length; i += 2) {
            if (arguments[i] == null || arguments[i + 1] == null) throw new IllegalArgumentException("Keys or replacement values must not be null.");
            content = content.replace(arguments[i].toString(), arguments[i + 1].toString());
        }
        return content;
    }
    
}
