package fr.maxlego08.text.text;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class ZText extends ZUtils implements Text {

    private final TextGeneratorPlugin plugin;
    private final String name;
    private final String language;
    private final String title;
    private final int length;
    private final List<TextLine> lines;
    private String result;

    public ZText(TextGeneratorPlugin plugin, String name, String language, String title, int length, List<TextLine> lines) {
        this.plugin = plugin;
        this.name = name;
        this.language = language;
        this.title = title;
        this.length = length;
        this.lines = lines;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public List<TextLine> getLines() {
        return lines;
    }

    @Override
    public String getResult() {
        return this.result;
    }

    @Override
    public boolean hasResult() {
        return this.result != null;
    }

    @Override
    public String getResult(Player player) {

        if (hasResult()) return this.result;

        StringBuilder builder = new StringBuilder();
        var textManager = this.plugin.getTextManager();

        if (this.title != null) {

            String title = player == null ? this.title : papi(this.title.replace("%player%", player.getName()), player);

            builder.append(title);
            int size = textManager.getInventoryTitleAlphabet().getTextLength(title);
            builder.append(textManager.getNegativeOffset(size));
        }

        var args = player == null ? new Object[0] : new Object[]{"%player%", player.getName()};
        builder.append(textManager.processText(this.lines, this.length, args));
        return builder.toString();
    }

    @Override
    public void createCacheResult() {
        if (this.containsPlaceholder(this.title)) return;

        for (TextLine line : this.lines) {
            if (this.containsPlaceholder(line.content())) return;
        }

        this.result = this.getResult(null);
    }
}
