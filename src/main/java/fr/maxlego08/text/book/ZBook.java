package fr.maxlego08.text.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.book.PageContent;
import fr.maxlego08.text.api.book.PageType;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;

import java.util.List;
import java.util.Optional;

public class ZBook implements Book {

    private final String name;
    private final String inventoryName;
    private final List<BookPage> pages;
    private final int startOffset;
    private final int leftSize;
    private final int rightSize;
    private final int offsetBetween;
    private final Alphabet alphabet;

    public ZBook(String name, String inventoryName, List<BookPage> pages, int startOffset, int leftSize, int rightSize, int offsetBetween, Alphabet alphabet) {
        this.name = name;
        this.inventoryName = inventoryName;
        this.pages = pages;
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
    public String getInventoryName() {
        return inventoryName;
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

    private String processText(Page page, Alphabet alphabet, int maxWidth, ColorHelper colorHelper, TextManager textManager) {

        if (page.pageType() == PageType.FILL) {
            return processText(page.pageContents().getFirst().content(), alphabet, maxWidth, colorHelper, textManager);
        }

        StringBuilder builder = new StringBuilder();
        int height = 0;

        for (PageContent pageContent : page.pageContents()) {

            var text = pageContent.content();
            var alignment = pageContent.alignment();
            var currentAlphabet = pageContent.alphabet();

            var result = colorHelper.transformString(currentAlphabet, text, height);
            switch (alignment) {
                case CENTER -> {
                    int centerWidth = maxWidth / 2;
                    int length = result.length() / 2;
                    int rest = result.length() % 2;

                    int start = centerWidth - (length + rest);

                    builder.append(textManager.getOffset(start));
                    builder.append(result.string());
                    builder.append(textManager.getNegativeOffset(start + result.length()));
                }
                case LEFT -> {
                    builder.append(result.string());
                    builder.append(textManager.getNegativeOffset(result.length()));
                }
                case RIGHT -> {
                    builder.append(textManager.getOffset(maxWidth - result.length()));
                    builder.append(result.string());
                    builder.append(textManager.getNegativeOffset(maxWidth));
                }
            }

            height += 1;
        }

        return builder.toString();
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
