package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.commands.CommandType;
import fr.maxlego08.text.api.commands.VCommand;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.text.animation.TextAnimationType;
import fr.maxlego08.text.api.utils.Permission;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

public class CommandTextGeneratorText extends VCommand {

    public CommandTextGeneratorText(TextGeneratorPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_TEXT);
        this.addSubCommand("open");
        this.setDescription(Message.DESCRIPTION_TEXT);
        this.addRequireArg("player");
        this.addRequireArg("text", (sender, args) -> plugin.getTextManager().getTexts().stream().map(Text::getName).distinct().collect(Collectors.toList()));
        this.addRequireArg("animation", (sender, args) -> Arrays.stream(TextAnimationType.values()).map(type -> type.name().toLowerCase(Locale.ROOT)).collect(Collectors.toList()));
        this.addRequireArg("speed");
    }

    @Override
    protected CommandType perform(TextGeneratorPlugin plugin) {

        Player target = this.argAsPlayer(0);
        if (target == null) {
            String playerArg = this.argAsString(0);
            message(plugin, sender, Message.TEXT_PLAYER_NOT_FOUND, "%player%", playerArg == null ? "unknown" : playerArg);
            return CommandType.SUCCESS;
        }

        String textName = this.argAsString(1);
        if (textName == null || textName.isEmpty()) {
            syntaxMessage();
            return CommandType.SUCCESS;
        }

        var optionalText = plugin.getTextManager().getText(textName, target);
        if (optionalText.isEmpty()) {
            message(plugin, sender, Message.TEXT_NOT_FOUND, "%text%", textName);
            return CommandType.SUCCESS;
        }

        String animationName = this.argAsString(2);
        TextAnimationType animationType;
        try {
            animationType = TextAnimationType.valueOf(animationName.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            message(plugin, sender, Message.TEXT_ANIMATION_TYPE_UNKNOWN, "%type%", animationName == null ? "unknown" : animationName);
            return CommandType.SUCCESS;
        }

        long speed;
        try {
            speed = this.argAsLong(3);
        } catch (Exception exception) {
            message(plugin, sender, Message.TEXT_ANIMATION_SPEED_INVALID);
            return CommandType.SUCCESS;
        }

        if (animationType != TextAnimationType.NONE && speed <= 0) {
            message(plugin, sender, Message.TEXT_ANIMATION_SPEED_INVALID);
            return CommandType.SUCCESS;
        }

        TextAnimationOptions options = animationType == TextAnimationType.NONE
                ? TextAnimationOptions.none()
                : new TextAnimationOptions(animationType, 0L, speed);

        plugin.getTextManager().displayText(target, optionalText.get(), options);
        return CommandType.SUCCESS;
    }
}
