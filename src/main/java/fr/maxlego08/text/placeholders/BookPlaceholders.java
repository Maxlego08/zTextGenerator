package fr.maxlego08.text.placeholders;

import fr.maxlego08.text.api.TextGeneratorPlugin;
import fr.maxlego08.text.api.TextManager;
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
            if (values.size() != 2) {
                return "The format is invalid! Please try again (" + values.size() + ")";
            }

            String bookName = values.getFirst();
            int pageNumber = Integer.parseInt(values.get(1));

            var optional = textManager.getBook(bookName);
            if (optional.isEmpty()) {
                return "Book " + bookName + " not found";
            }

            var book = optional.get();

            var optionalPage = book.getPage(pageNumber);
            if (optionalPage.isEmpty()) {
                return "Page " + pageNumber + " not found";
            }

            var alphabet = book.getAlphabet();
            var page = optionalPage.get();
            String text = page.leftText();
            var colorHelper = this.plugin.getColorHelper();
            var words = text.split(" ");

            StringBuilder builder = new StringBuilder();
            builder.append(textManager.getOffset(book.getStartOffset()));

            String currentLine = "";
            int currentLength = 0;
            int height = 0;
            int spaceLength = alphabet.getLength(' ');

            for (String word : words) {

                int wordLength = alphabet.getTextLength(word);

                if (currentLength + wordLength > book.getLeftSize()) {

                    var result = colorHelper.transformString(alphabet, currentLine, height);
                    builder.append(result.string());
                    builder.append(textManager.getNegativeOffset(result.length()));

                    currentLine = "";
                    currentLength = 0;
                    height += 1;
                }

                currentLine += word;
                currentLength += wordLength;

                currentLine += " ";
                currentLength += spaceLength;
            }

            var result = colorHelper.transformString(alphabet, currentLine, height);
            builder.append(result.string());
            builder.append(textManager.getNegativeOffset(result.length()));
            builder.append(textManager.getOffset(book.getLeftSize() + book.getOffsetBetween()));

            return textManager.transformFont(builder.toString());
        };
    }
}
