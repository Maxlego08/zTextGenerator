package fr.maxlego08.text.hooks.zmenu.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.text.animation.TextAnimationType;
import org.bukkit.entity.Player;

public class OpenTextAction extends Action {

    private final TextGeneratorPlugin plugin;
    private final String text;
    private final TextAnimationType textAnimationType;
    private final long displaySpeed;

    public OpenTextAction(TextGeneratorPlugin plugin, String text, TextAnimationType textAnimationType, long displaySpeed) {
        this.plugin = plugin;
        this.text = text;
        this.textAnimationType = textAnimationType;
        this.displaySpeed = displaySpeed;
    }

    @Override
    protected void execute(Player player, Button button, InventoryEngine inventoryEngine, Placeholders placeholders) {

        var optional = this.plugin.getTextManager().getText(this.text, player);
        if (optional.isEmpty()) {
            this.plugin.getLogger().warning("Text " + this.text + " not found");
            return;
        }

        var text = optional.get();
        TextAnimationOptions textAnimationOptions = this.textAnimationType == TextAnimationType.NONE ? TextAnimationOptions.none() : new TextAnimationOptions(textAnimationType, 0L, this.displaySpeed);
        this.plugin.getTextManager().displayText(player, text, textAnimationOptions);
    }
}
