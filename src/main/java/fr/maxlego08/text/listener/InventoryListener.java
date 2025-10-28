package fr.maxlego08.text.listener;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.book.BookInventory;
import fr.maxlego08.text.text.TextInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    private final TextPlugin plugin;

    public InventoryListener(TextPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof BookInventory || event.getInventory().getHolder() instanceof TextInventory) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getInventory().getHolder() instanceof TextInventory)) {
            return;
        }

        if (event.getPlayer() instanceof Player player) {
            // this.plugin.getTextManager().handleTextInventoryClose(player);
        }
    }
}
