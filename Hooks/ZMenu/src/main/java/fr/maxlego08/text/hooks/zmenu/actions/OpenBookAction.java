package fr.maxlego08.text.hooks.zmenu.actions;

import fr.maxlego08.menu.api.button.Button;
import fr.maxlego08.menu.api.engine.InventoryEngine;
import fr.maxlego08.menu.api.requirement.Action;
import fr.maxlego08.menu.api.utils.Placeholders;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import org.bukkit.entity.Player;

public class OpenBookAction extends Action {

    private final TextGeneratorPlugin plugin;
    private final String book;
    private final int page;

    public OpenBookAction(TextGeneratorPlugin plugin, String book, int page) {
        this.plugin = plugin;
        this.book = book;
        this.page = page;
    }

    @Override
    protected void execute(Player player, Button button, InventoryEngine inventoryEngine, Placeholders placeholders) {

        var optional = this.plugin.getTextManager().getBook(this.book, player);
        if (optional.isEmpty()) {
            this.plugin.getLogger().warning("Book " + this.book + " not found");
            return;
        }

        var book = optional.get();
        var optionalPage = book.getPage(this.page);
        if (optionalPage.isEmpty()) {
            this.plugin.getLogger().warning("Page " + this.page + " not found");
            return;
        }

        this.plugin.getTextManager().openBook(player, book, optionalPage.get());
    }
}
