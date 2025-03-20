package fr.maxlego08.text.api.placeholders;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class LocalPlaceholder {

    private static LocalPlaceholder instance;
    private final String prefix = "ztextgenerator";
    private final Pattern pattern = Pattern.compile("[%]([^%]+)[%]");
    private final List<AutoPlaceholder> autoPlaceholders = new ArrayList<>();
    private TextGeneratorPlugin plugin;

    private LocalPlaceholder() {
    }

    public static LocalPlaceholder getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (LocalPlaceholder.class) {
                if (instance == null) {
                    instance = new LocalPlaceholder();
                }
            }
        }
        return instance;
    }

    public String setPlaceholders(Player player, String placeholder) {

        if (placeholder == null || !placeholder.contains("%")) {
            return placeholder;
        }

        final String realPrefix = prefix + "_";

        Matcher matcher = this.pattern.matcher(placeholder);
        while (matcher.find()) {
            String stringPlaceholder = matcher.group(0);
            String regex = matcher.group(1).replace(realPrefix, "");
            String replace = this.onRequest(player, regex);
            if (replace != null) {
                placeholder = placeholder.replace(stringPlaceholder, replace);
            }
        }

        return placeholder;
    }

    public List<String> setPlaceholders(Player player, List<String> lore) {
        return lore == null ? null : lore.stream().map(e -> e = setPlaceholders(player, e)).collect(Collectors.toList());
    }

    public String onRequest(Player player, String string) {

        Optional<AutoPlaceholder> optional = this.autoPlaceholders.stream().filter(autoPlaceholder -> autoPlaceholder.startsWith(string)).findFirst();
        if (optional.isPresent()) {

            AutoPlaceholder autoPlaceholder = optional.get();
            String value = string.replace(autoPlaceholder.getStartWith(), "");
            return autoPlaceholder.accept(player, value);
        }

        return null;
    }

    public void register(String startWith, BiFunction<Player, String, String> biConsumer) {
        this.autoPlaceholders.add(new AutoPlaceholder(startWith, biConsumer));
    }

    public void register(String startWith, Function<Player, String> biConsumer) {
        this.autoPlaceholders.add(new AutoPlaceholder(startWith, biConsumer));
    }

    public TextGeneratorPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(TextGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    public String getPrefix() {
        return prefix;
    }
}
