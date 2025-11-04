package fr.maxlego08.text.text.alphabet;

import fr.maxlego08.text.ZTextManager;
import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.fonts.FontInfo;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;

public class AlphabetValidationTask extends BukkitRunnable {

    private final TextGeneratorPlugin plugin;
    private final ZTextManager manager;
    private final Player player;
    private final Alphabet alphabet;
    private final Iterator<FontInfo> iterator;
    private final int letterByLine;
    private final int maxLines;
    private final String inventoryName;
    private final int inventorySize;
    private final int startOffset;
    private final int endOffset;

    public AlphabetValidationTask(TextGeneratorPlugin plugin, ZTextManager manager, Player player, Alphabet alphabet, List<FontInfo> letters,
                                  int letterByLine, int maxLines, String inventoryName, int inventorySize, int startOffset, int endOffset) {
        this.plugin = plugin;
        this.manager = manager;
        this.player = player;
        this.alphabet = alphabet;
        this.iterator = letters.iterator();
        this.letterByLine = letterByLine;
        this.maxLines = maxLines;
        this.inventoryName = inventoryName;
        this.inventorySize = inventorySize;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    @Override
    public void run() {

        if (!player.isOnline()) {
            this.manager.finishAlphabetValidation(player.getUniqueId(), this, AlphabetValidationStopReason.PLAYER_LEFT);
            return;
        }

        if (!iterator.hasNext()) {
            this.manager.finishAlphabetValidation(player.getUniqueId(), this, AlphabetValidationStopReason.COMPLETED);
            return;
        }

        FontInfo fontInfo = iterator.next();
        this.manager.displayAlphabet(player, alphabet, String.valueOf(fontInfo.character()), this.letterByLine, this.maxLines,
                fontInfo.length(), this.inventoryName, this.inventorySize, this.startOffset, this.endOffset);
    }

    public void schedule(long delayTicks) {
        this.runTaskTimer(this.plugin, 0L, delayTicks);
    }

    public String getAlphabetName() {
        return this.alphabet.getName();
    }
}
