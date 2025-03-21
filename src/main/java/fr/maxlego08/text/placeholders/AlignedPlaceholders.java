package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;
import fr.maxlego08.text.api.utils.Alignment;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;

public class AlignedPlaceholders extends PlaceholderRegister {

    @Override
    public void register(TextManager textManager) {

        register("center_", createAlignedPlaceholder(textManager, Alignment.CENTER), "Center the text in the given height.", "alphabet", "height", "text");
        register("left_", createAlignedPlaceholder(textManager, Alignment.LEFT), "Left align the text in the given height.", "alphabet", "height", "text");
        register("right_", createAlignedPlaceholder(textManager, Alignment.RIGHT), "Right align the text in the given height.", "alphabet", "height", "text");
        register("length_", getTextLength(textManager), "Return the length of the given text.", "alphabet", "text");
    }

    private BiFunction<Player, String, String> getTextLength(TextManager textManager) {
        return (player, args) -> {

            List<String> values = splitIgnoringBraces(args);
            if (values.size() != 2) {
                return "The format is invalid! Please try again (" + values.size() + ")";
            }

            String alphabetName = values.getFirst();
            String content = papi(values.get(1).replace("%player%", player.getName()), player);

            var optional = textManager.getAlphabet(alphabetName);
            if (optional.isEmpty()) {
                return "Alphabet " + alphabetName + " not found";
            }

            var alphabet = optional.get();
            return String.valueOf(alphabet.getTextLength(content));
        };
    }

    private BiFunction<Player, String, String> createAlignedPlaceholder(TextManager textManager, Alignment alignment) {
        return (player, args) -> {
            List<String> values = splitIgnoringBraces(args);
            if (values.size() != 3) {
                return "The format is invalid! Please try again (" + values.size() + ")";
            }

            String alphabetName = values.getFirst();
            int height = Integer.parseInt(values.get(1));
            String content = papi(values.get(2).replace("%player%", player.getName()), player);

            var optional = textManager.getAlphabet(alphabetName);
            if (optional.isEmpty()) {
                return "Alphabet " + alphabetName + " not found";
            }

            var alphabet = optional.get();
            var textLength = alphabet.getTextLength(content);

            StringBuilder sb = new StringBuilder();

            switch (alignment) {
                case CENTER -> {
                    int length = textLength / 2;
                    int rest = textLength % 2;
                    sb.append(textManager.getNegativeOffset(length + rest));
                    for (char c : content.toCharArray()) {
                        sb.append(alphabet.transformChar(c, height));
                    }
                    sb.append(textManager.getNegativeOffset(textLength + rest));
                }
                case LEFT -> {
                    for (char c : content.toCharArray()) {
                        sb.append(alphabet.transformChar(c, height));
                    }
                    sb.append(textManager.getNegativeOffset(textLength));
                }
                case RIGHT -> {
                    sb.append(textManager.getNegativeOffset(textLength));
                    for (char c : content.toCharArray()) {
                        sb.append(alphabet.transformChar(c, height));
                    }
                }
            }

            return sb.toString();
        };
    }
}
