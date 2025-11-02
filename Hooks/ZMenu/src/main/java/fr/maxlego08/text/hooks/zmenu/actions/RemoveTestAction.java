package fr.maxlego08.text.hooks.zmenu.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import org.bukkit.entity.Player;

public class RemoveTestAction extends Action {

    private final TextGeneratorPlugin plugin;

    public RemoveTestAction(TextGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void execute(Player player, Button button, InventoryEngine inventoryEngine, Placeholders placeholders) {
        this.plugin.setTestValue(this.plugin.getTestValue() - 1);
    }
}
