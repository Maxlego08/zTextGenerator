package fr.maxlego08.text.api.placeholders;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.utils.ZUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class PlaceholderRegister extends ZUtils {

    public abstract void register(TextManager textManager);

    /**
     * Splits the given input string by '_' character, but ignores when it's inside a pair of curly braces.
     * The resulting list is then mapped to replace '{' and '}' characters with '%'.
     *
     * @param input the string to be split
     * @return the list of strings as described
     */
    protected List<String> splitIgnoringBraces(String input) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideBraces = false;

        for (char c : input.toCharArray()) {
            if (c == '{') {
                insideBraces = true;
            } else if (c == '}') {
                insideBraces = false;
            }

            if (c == '_' && !insideBraces) {
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

        return result.stream().map(e -> e.replace("{", "%").replace("}", "%")).toList();
    }

}
