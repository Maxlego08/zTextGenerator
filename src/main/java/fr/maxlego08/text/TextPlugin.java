package fr.maxlego08.text;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.fonts.FontImage;
import fr.maxlego08.text.api.hooks.HookProvider;
import fr.maxlego08.text.api.messages.MessageManager;
import fr.maxlego08.text.api.utils.Plugins;
import fr.maxlego08.text.color.PaperColor;
import fr.maxlego08.text.command.ZCommandManager;
import fr.maxlego08.text.command.commands.CommandTextGenerator;
import fr.maxlego08.text.listener.InventoryListener;
import fr.maxlego08.text.messages.ZMessageManager;
import fr.maxlego08.text.placeholders.AlignedPlaceholders;
import fr.maxlego08.text.placeholders.BookPlaceholders;
import fr.maxlego08.text.placeholders.TextPlaceholders;
import fr.maxlego08.text.placeholders.TitlePlaceholders;
import fr.maxlego08.text.zcore.ZPlugin;
import fr.maxlego08.text.zcore.utils.EmptyFont;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TextPlugin extends ZPlugin {

    private final TextManager textManager = new ZTextManager(this);
    private final ColorHelper colorHelper = new PaperColor();
    private final MessageManager messageManager = new ZMessageManager(this);
    private boolean enableDebug = false;
    private FontImage fontImage = new EmptyFont();
    private final List<HookProvider> hookProviders = new ArrayList<>();

    @Override
    public void onEnable() {
        preEnable();

        this.ZCommandManager = new ZCommandManager(this);

        this.saveDefaultConfig();

        this.enableDebug = this.getConfig().getBoolean("enable-debug", false);
        this.textManager.loadAlphabets();
        this.textManager.loadTexts();
        this.textManager.loadBooks();

        var placeholders = List.of(new AlignedPlaceholders(this), new TitlePlaceholders(), new TextPlaceholders(), new BookPlaceholders(this));
        placeholders.forEach(placeholder -> placeholder.register(this.textManager));

        this.registerCommand("text-generator", new CommandTextGenerator(this), "text", "tg");
        this.registerListener(new InventoryListener());

        this.createInstances();

        this.hookProviders.forEach(hookProvider -> hookProvider.onEnable(this));

        postEnable();
    }

    @Override
    public void onDisable() {
        preDisable();

        this.hookProviders.forEach(hookProvider -> hookProvider.onDisable(this));

        postDisable();
    }

    @Override
    public TextManager getTextManager() {
        return textManager;
    }

    @Override
    public ColorHelper getColorHelper() {
        return colorHelper;
    }

    @Override
    public boolean isEnableDebug() {
        return enableDebug;
    }

    @Override
    public void debug(String message) {
        if (this.enableDebug) {
            this.getLogger().info(message);
        }
    }

    @Override
    public void reloadConfigurations() {

        this.enableDebug = this.getConfig().getBoolean("enable-debug", false);

        this.textManager.loadAlphabets();
        this.textManager.loadTexts();
        this.textManager.loadBooks();

        this.messageLoader.load();

        this.hookProviders.forEach(hookProvider -> hookProvider.onReload(this));
    }

    @Override
    public MessageManager getMessageManager() {
        return this.messageManager;
    }

    @Override
    public FontImage getFontImage() {
        return this.fontImage;
    }

    private void createInstances() {

        if (isActive(Plugins.ITEMSADDER)) {
            Optional<FontImage> optional = createInstance("ItemsAdderFont");
            optional.ifPresentOrElse(fontImage -> {
                this.fontImage = fontImage;
                getLogger().info("Loaded ItemsAdderFont");
            }, () -> getLogger().severe("Failed to load ItemsAdderFont"));
        }

        if (isActive(Plugins.NEXO)) {
            Optional<FontImage> optional = createInstance("NexoFont");
            optional.ifPresentOrElse(fontImage -> {
                this.fontImage = fontImage;
                getLogger().info("Loaded NexoFont");
            }, () -> getLogger().severe("Failed to load NexoFont"));
        }

        if (isActive(Plugins.ZMENU)){
            Optional<HookProvider> optional = createInstance("zmenu.ZMenuProvider");
            optional.ifPresentOrElse(hookProvider -> {
                this.hookProviders.add(hookProvider);
                getLogger().info("Loaded zMenu");
            }, () -> getLogger().severe("Failed to load zMenu"));
        }

    }
}
