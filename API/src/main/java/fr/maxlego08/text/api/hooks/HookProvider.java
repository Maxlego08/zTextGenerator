package fr.maxlego08.text.api.hooks;

import fr.maxlego08.text.api.TextGeneratorPlugin;

public interface HookProvider {

    void onEnable(TextGeneratorPlugin plugin);

    void onDisable(TextGeneratorPlugin plugin);

    void onReload(TextGeneratorPlugin plugin);

}
