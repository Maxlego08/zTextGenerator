package fr.maxlego08.text;

import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.messages.MessageManager;
import fr.maxlego08.text.color.PaperColor;
import fr.maxlego08.text.command.CommandManager;
import fr.maxlego08.text.command.commands.CommandTextGenerator;
import fr.maxlego08.text.messages.ZMessageManager;
import fr.maxlego08.text.placeholders.AlignedPlaceholders;
import fr.maxlego08.text.zcore.ZPlugin;

import java.util.List;

public final class TextPlugin extends ZPlugin {

    private final TextManager textManager = new ZTextManager(this);
    private final ColorHelper colorHelper = new PaperColor();
    private final MessageManager messageManager = new ZMessageManager(this);
    private boolean enableDebug = false;

    @Override
    public void onEnable() {
        preEnable();

        this.commandManager = new CommandManager(this);

        this.saveDefaultConfig();

        this.enableDebug = this.getConfig().getBoolean("enable-debug", false);
        this.textManager.loadAlphabets();

        var placeholders = List.of(new AlignedPlaceholders());
        placeholders.forEach(placeholder -> placeholder.register(this.textManager));

        this.registerCommand("text-generator", new CommandTextGenerator(this), "text", "tg");

        postEnable();
    }

    @Override
    public void onDisable() {
        preDisable();


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
        this.messageLoader.load();
    }

    @Override
    public MessageManager getMessageManager() {
        return this.messageManager;
    }
}
