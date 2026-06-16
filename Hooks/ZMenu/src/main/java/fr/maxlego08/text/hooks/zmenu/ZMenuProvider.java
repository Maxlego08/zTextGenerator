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
import org.bukkit.event.Listener;

import java.io.File;

public class ZMenuProvider extends ZUtils implements HookProvider, Listener {

    private InventoryManager inventoryManager;

    @Override
    public void onEnable(TextGeneratorPlugin plugin) {

        this.inventoryManager = plugin.getProvider(InventoryManager.class);
        ButtonManager buttonManager = plugin.getProvider(ButtonManager.class);
        if (buttonManager == null) {
            plugin.getLogger().warning("ButtonManager not found, retry in 5 seconds");
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> onEnable(plugin), 20 * 5L);
            return;
        }

        buttonManager.registerAction(new OpenTextLoader(plugin));
        buttonManager.registerAction(new OpenBookLoader(plugin));
        buttonManager.registerAction(new AddTestLoader(plugin));
        buttonManager.registerAction(new RemoveTestLoader(plugin));

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
            var fileName = plugin.getFontType().name().toLowerCase();
            saveResource(plugin, "inventories/" + fileName + "/example-actions.yml", "inventories/example-actions.yml", false);
            saveResource(plugin, "inventories/" + fileName + "/example-center.yml", "inventories/example-center.yml", false);
            saveResource(plugin, "inventories/" + fileName + "/example-left.yml", "inventories/example-left.yml", false);
            saveResource(plugin, "inventories/" + fileName + "/example-right.yml", "inventories/example-right.yml", false);
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
