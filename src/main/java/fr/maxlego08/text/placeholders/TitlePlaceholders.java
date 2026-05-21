package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class TitlePlaceholders extends PlaceholderRegister {

    @Override
    public void register(TextManager textManager) {

        register("title_length_", getTitleLength(textManager), "Get the inventory title length.", "title");
    }

    private BiFunction<Player, String, String> getTitleLength(TextManager textManager) {
        return (player, args) -> {
            String content = args.replace("_", " ");
            try {
                Alphabet alphabet = textManager.getInventoryTitleAlphabet();
                return String.valueOf(alphabet.getTextLength(content));
            } catch (RuntimeException exception) {
                return "Inventory title alphabet not found";
            }
        };
    }
}
