package fr.maxlego08.text.listener;

import fr.maxlego08.text.book.BookInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getInventory().getHolder() instanceof BookInventory) {
            event.setCancelled(true);
        }
    }

}
