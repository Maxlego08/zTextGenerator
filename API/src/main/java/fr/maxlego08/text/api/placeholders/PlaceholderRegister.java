package fr.maxlego08.text.api.placeholders;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public abstract class PlaceholderRegister extends ZUtils {

    public abstract void register(TextManager textManager);

    protected void register(String startWith, BiFunction<Player, String, String> biConsumer, String description, String... args) {
        LocalPlaceholder.getInstance().register(startWith, biConsumer, description, args);
    }

    protected void register(String startWith, Function<Player, String> biConsumer, String description, String... args) {
        LocalPlaceholder.getInstance().register(startWith, biConsumer, description, args);
    }


    /**
     * Splits the given input string by '_' character, but ignores when it's inside a pair of curly braces.
     * The resulting list is then mapped to replace '{' and '}' characters with '%'.
     * A maximum number of parts can be specified. If maxParts is greater than 0, the result will contain
     * at most maxParts elements, with the last element containing the rest of the string.
     *
     * @param input    the string to be split
     * @param maxParts maximum number of parts to split into
     * @return the list of strings as described
     */
    protected List<String> splitIgnoringBraces(String input, int maxParts) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideBraces = false;

        for (char c : input.toCharArray()) {
            if (c == '{') {
                insideBraces = true;
            } else if (c == '}') {
                insideBraces = false;
            }

            if (c == '_' && !insideBraces
                    && (maxParts <= 0 || result.size() < maxParts - 1)) {
                if (!current.isEmpty()) {
                    result.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }

        if (!current.isEmpty()) {
            result.add(current.toString());
        }

        return result.stream()
                .map(e -> e.replace("{", "%").replace("}", "%"))
                .toList();
    }


}
