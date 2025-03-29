package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.book.PageContent;
import fr.maxlego08.text.api.book.PageType;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;
import fr.maxlego08.text.api.placeholders.PlaceholderRegister;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;

public class BookPlaceholders extends PlaceholderRegister {

    private final TextGeneratorPlugin plugin;

    public BookPlaceholders(TextGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void register(TextManager textManager) {
        register("book_", getBook(textManager), "Get the book.", "book", "page");
    }

    private BiFunction<Player, String, String> getBook(TextManager textManager) {
        return (player, args) -> {
            List<String> values = splitIgnoringBraces(args);
            if (values.size() != 2) return "The format is invalid! Please try again (" + values.size() + ")";

            String bookName = values.getFirst();
            int pageNumber = Integer.parseInt(values.get(1));
            var bookOpt = textManager.getBook(bookName);
            if (bookOpt.isEmpty()) return "Book " + bookName + " not found";

            var book = bookOpt.get();
            var pageOpt = book.getPage(pageNumber);
            if (pageOpt.isEmpty()) return "Page " + pageNumber + " not found";

            var page = pageOpt.get();
            var alphabet = book.getAlphabet();
            var builder = new StringBuilder(textManager.getOffset(book.getStartOffset()));
            var colorHelper = plugin.getColorHelper();

            builder.append(processText(page.left(), alphabet, book.getLeftSize(), colorHelper, textManager));
            builder.append(textManager.getOffset(book.getLeftSize() + book.getOffsetBetween()));
            builder.append(processText(page.right(), alphabet, book.getLeftSize(), colorHelper, textManager));

            return textManager.transformFont(builder.toString());
        };
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
