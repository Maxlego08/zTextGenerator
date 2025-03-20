package fr.maxlego08.text.zcore;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.placeholders.LocalPlaceholder;
import fr.maxlego08.text.api.placeholders.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ZPlugin extends JavaPlugin implements TextGeneratorPlugin {

    protected void preEnable() {

        LocalPlaceholder.getInstance().setPlugin(this);
        Placeholder.getPlaceholder();

    }

    protected void postEnable() {

    }

    protected void preDisable() {

    }

    protected void postDisable() {

    }

}
