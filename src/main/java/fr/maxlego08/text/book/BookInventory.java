package fr.maxlego08.text.book;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class BookInventory implements InventoryHolder {

    private Inventory inventory;

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
