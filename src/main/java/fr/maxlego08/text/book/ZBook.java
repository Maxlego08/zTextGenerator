package fr.maxlego08.text.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.book.PageType;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class ZBook extends ZUtils implements Book {

    private final TextGeneratorPlugin plugin;
    private final String name;
    private final String language;
    private final String inventoryName;
    private final List<BookPage> pages;
    private final int inventoryStartOffset;
    private final int inventoryEndOffset;
    private final int startOffset;
    private final int leftSize;
    private final int rightSize;
    private final int offsetBetween;
    private final Alphabet alphabet;

    public ZBook(TextGeneratorPlugin plugin, String name, String language, String inventoryName, List<BookPage> pages, int inventoryStartOffset, int inventoryEndOffset, int startOffset, int leftSize, int rightSize, int offsetBetween, Alphabet alphabet) {
        this.plugin = plugin;
        this.name = name;
        this.language = language;
        this.inventoryName = inventoryName;
        this.pages = pages;
        this.inventoryStartOffset = inventoryStartOffset;
        this.inventoryEndOffset = inventoryEndOffset;
        this.startOffset = startOffset;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
        this.offsetBetween = offsetBetween;
        this.alphabet = alphabet;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getLanguage() {
        return this.language;
    }

    @Override
    public String getInventoryName() {
        return inventoryName;
    }

    @Override
    public String getInventoryName(Player player) {
        if (player == null || this.inventoryName.isEmpty()) {
            return this.inventoryName;
        }

        var fontType = this.plugin.getFontType();
        var inventoryName = this.inventoryName.contains("%") ? papi(this.inventoryName.replace("%player%", player.getName()), player) : this.inventoryName;

        return fontType.getOffset(this.inventoryStartOffset) + "<white>" + fontType.getFormat(inventoryName) + fontType.getOffset(this.inventoryEndOffset);
    }

    @Override
    public List<BookPage> getPages() {
        return this.pages;
    }

    @Override
    public int getStartOffset() {
        return this.startOffset;
    }

    @Override
    public int getLeftSize() {
        return this.leftSize;
    }

    @Override
    public int getRightSize() {
        return this.rightSize;
    }

    @Override
    public int getOffsetBetween() {
        return this.offsetBetween;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    @Override
    public Optional<BookPage> getPage(int page) {
        return this.pages.stream().filter(bookPage -> bookPage.page() == page).findFirst();
    }

    @Override
    public String toBookString(TextManager textManager, ColorHelper colorHelper, BookPage page) {

        String builder = textManager.getOffset(this.startOffset) + //
                processText(page.left(), alphabet, this.leftSize, colorHelper, textManager) + //
                textManager.getOffset(this.leftSize + this.offsetBetween) + //
                processText(page.right(), alphabet, this.rightSize, colorHelper, textManager);

        return textManager.transformFont(builder);
    }

    @Override
    public int getInventoryStartOffset() {
        return this.inventoryStartOffset;
    }

    @Override
    public int getInventoryEndOffset() {
        return this.inventoryEndOffset;
    }

    private String processText(Page page, Alphabet alphabet, int maxWidth, ColorHelper colorHelper, TextManager textManager) {

        if (page.pageType() == PageType.FILL) {
            return processText(page.textLines().getFirst().content(), alphabet, maxWidth, colorHelper, textManager);
        }

        return textManager.processText(page.textLines(), maxWidth);
    }

    private String processText(String text, Alphabet alphabet, int maxWidth, ColorHelper colorHelper, TextManager textManager) {

        StringBuilder builder = new StringBuilder();
        StringBuilder line = new StringBuilder();
        int lineLength = 0, height = 0, space = alphabet.getLength(' ');
        Result result;

        for (String word : text.split(" ")) {
            int wordLen = alphabet.getTextLength(word);
            if (lineLength + wordLen > maxWidth) {
                result = colorHelper.transformString(alphabet, line.toString(), height);
                builder.append(result.string()).append(textManager.getNegativeOffset(result.length()));
                line.setLength(0);
                lineLength = 0;
                height++;
            }
            line.append(word).append(" ");
            lineLength += wordLen + space;
        }

        result = colorHelper.transformString(alphabet, line.toString(), height);
        builder.append(result.string()).append(textManager.getNegativeOffset(result.length()));
        return builder.toString();
    }
}
