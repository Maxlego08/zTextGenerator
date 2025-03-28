package fr.maxlego08.text.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextElement;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.Alignment;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.List;

public class ZText extends ZUtils implements Text {

    private final TextGeneratorPlugin plugin;
    private final String name;
    private final String title;
    private final Alignment alignment;
    private final Alphabet alphabet;
    private final int length;
    private final List<TextLine> lines;
    private String result;

    public ZText(TextGeneratorPlugin plugin, String name, String title, Alignment alignment, Alphabet alphabet, int length, List<TextLine> lines) {
        this.plugin = plugin;
        this.name = name;
        this.title = title;
        this.alignment = alignment;
        this.alphabet = alphabet;
        this.length = length;
        this.lines = lines;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
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
    public void createResult() {

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
        var colorHelper = this.plugin.getColorHelper();

        if (this.title != null) {

            String title = papi(this.title.replace("%player%", player.getName()), player);

            builder.append(title);
            int size = textManager.getInventoryTitleAlphabet().getTextLength(title);
            builder.append(textManager.getNegativeOffset(size));
        }

        int lineHeight = 1;
        for (TextLine line : this.lines) {

            StringBuilder lineBuilder = new StringBuilder();
            int lineLength = 0;

            for (int i = 0; i < line.getElements().size(); i++) {
                TextElement textElement = line.getElements().get(i);

                String element = papi(textElement.element().replace("%player%", player.getName()), player);
                Alphabet alphabet = textElement.alphabet() != null ? textElement.alphabet() : line.getAlphabet() != null ? line.getAlphabet() : this.alphabet;
                Alignment alignment = line.getAlignment() != null ? line.getAlignment() : this.alignment;

                var result = colorHelper.transformString(alphabet, element, lineHeight);
                lineBuilder.append(result.string());
                lineLength += result.length();

                if (i == line.getElements().size() - 1) {
                    lineBuilder.append(textManager.getNegativeOffset(lineLength));
                }

                // lineBuilder.append(plugin.getTextManager().replaceText(alphabet, alignment, element, lineHeight));
            }

            builder.append(lineBuilder);
            lineHeight++;
        }

        return builder.toString();
    }
}
