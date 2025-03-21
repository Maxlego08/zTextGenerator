package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;
import org.bukkit.entity.Player;

import java.util.function.BiFunction;

public class TextPlaceholders extends PlaceholderRegister {

    @Override
    public void register(TextManager textManager) {


        // Ajouter un placeholder et une méthode API pour avoir du texte qui s'adapte automatiquement à la fin de ligne.
        register("text_", getText(textManager), "Get the text.", "text");

    }

    private BiFunction<Player, String, String> getText(TextManager textManager) {
        return (player, textName) -> {

            var optional = textManager.getText(textName);
            if (optional.isEmpty()) {
                return "Text " + textName + " not found";
            }

            var text = optional.get();

            return "";
        };
    }

}
