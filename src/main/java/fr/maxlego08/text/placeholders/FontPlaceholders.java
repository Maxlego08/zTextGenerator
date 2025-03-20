package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.placeholders.LocalPlaceholder;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;

public class FontPlaceholders extends PlaceholderRegister {

    @Override
    public void register(TextManager textManager) {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        placeholder.register("text_center_", this.registerCenterPlaceholder(textManager));
    }

    private BiFunction<Player, String, String> registerCenterPlaceholder(TextManager textManager) {
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

            int length = textLength / 2;
            int rest = textLength % 2;
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append(":offset_-%d:".formatted(length + rest));
            for (char c : content.toCharArray()) {
                stringBuilder.append(alphabet.transformChar(c, height));
            }
            stringBuilder.append(":offset_-%d:".formatted(textLength + rest));

            return stringBuilder.toString();
        };
    }
}
