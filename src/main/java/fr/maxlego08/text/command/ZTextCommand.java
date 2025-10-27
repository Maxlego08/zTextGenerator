package fr.maxlego08.text.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import fr.maxlego08.text.ZTextPlugin;
import fr.maxlego08.text.animation.AnimatedTextTask;
import fr.maxlego08.text.animation.AnimationMode;

public class ZTextCommand implements CommandExecutor, TabCompleter {

    private static final long DEFAULT_INTERVAL = 10L;

    private final ZTextPlugin plugin;

    public ZTextCommand(ZTextPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage: /" + label + " <mode> [ticks] <text>");
            return true;
        }

        AnimationMode mode = AnimationMode.fromString(args[0]);
        if (mode == null) {
            sender.sendMessage("§cMode inconnu. Modes disponibles: none, letter_by_letter, word_by_word");
            return true;
        }

        long interval = DEFAULT_INTERVAL;
        int textStartIndex = 1;
        if (mode.requiresAnimation() && args.length >= 3) {
            Long parsed = parseLongSafe(args[1]);
            if (parsed != null) {
                interval = Math.max(1L, parsed);
                textStartIndex = 2;
            }
        }

        if (textStartIndex >= args.length) {
            sender.sendMessage("§cVeuillez fournir le texte à afficher.");
            return true;
        }

        String text = String.join(" ", Arrays.copyOfRange(args, textStartIndex, args.length));
        if (text.isBlank()) {
            sender.sendMessage("§cLe texte ne peut pas être vide.");
            return true;
        }

        if (!mode.requiresAnimation()) {
            sender.sendMessage(text);
            return true;
        }

        new AnimatedTextTask(sender, text, mode).runTaskTimer(plugin, 0L, interval);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String input = args[0].toLowerCase(Locale.ROOT);
            List<String> suggestions = new ArrayList<>();
            for (AnimationMode mode : AnimationMode.values()) {
                String name = mode.name().toLowerCase(Locale.ROOT);
                if (name.startsWith(input)) {
                    suggestions.add(name);
                }
            }
            return suggestions;
        }

        if (args.length == 2) {
            AnimationMode mode = AnimationMode.fromString(args[0]);
            if (mode != null && mode.requiresAnimation()) {
                return Collections.singletonList("10");
            }
        }

        return Collections.emptyList();
    }

    private static Long parseLongSafe(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
