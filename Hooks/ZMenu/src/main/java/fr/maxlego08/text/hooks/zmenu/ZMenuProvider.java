package fr.maxlego08.text.hooks.zmenu;

import fr.maxlego08.menu.api.ButtonManager;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.exceptions.InventoryException;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.hooks.HookProvider;
import fr.maxlego08.text.api.utils.ZUtils;
import fr.maxlego08.text.hooks.zmenu.loader.AddTestLoader;
import fr.maxlego08.text.hooks.zmenu.loader.OpenBookLoader;
import fr.maxlego08.text.hooks.zmenu.loader.OpenTextLoader;
import fr.maxlego08.text.hooks.zmenu.loader.RemoveTestLoader;

import java.io.File;

public class ZMenuProvider extends ZUtils implements HookProvider {

    private InventoryManager inventoryManager;
    private ButtonManager buttonManager;

    @Override
    public void onEnable(TextGeneratorPlugin plugin) {

        this.inventoryManager = plugin.getProvider(InventoryManager.class);
        this.buttonManager = plugin.getProvider(ButtonManager.class);

        this.buttonManager.registerAction(new OpenTextLoader(plugin));
        this.buttonManager.registerAction(new OpenBookLoader(plugin));
        this.buttonManager.registerAction(new AddTestLoader(plugin));
        this.buttonManager.registerAction(new RemoveTestLoader(plugin));

        this.loadInventories(plugin);
    }

    @Override
    public void onDisable(TextGeneratorPlugin plugin) {

    }

    @Override
    public void onReload(TextGeneratorPlugin plugin) {
        this.loadInventories(plugin);
    }

    private void loadInventories(TextGeneratorPlugin plugin) {

        var folder = new File(plugin.getDataFolder(), "inventories");
        if (!folder.exists()) {
            plugin.saveResource("inventories/example-actions.yml", false);
            plugin.saveResource("inventories/example-center.yml", false);
            plugin.saveResource("inventories/example-left.yml", false);
            plugin.saveResource("inventories/example-right.yml", false);
            folder.mkdirs();
        }

        if (this.inventoryManager == null) return;

        this.inventoryManager.deleteInventories(plugin);

        files(folder, file -> {
            try {
                this.inventoryManager.loadInventory(plugin, file);
            } catch (InventoryException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
