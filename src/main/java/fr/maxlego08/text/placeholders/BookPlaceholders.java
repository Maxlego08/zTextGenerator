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

            List<String> values = splitIgnoringBraces(args, 2);

            if (values.size() != 2) return "The format is invalid! Please try again (" + values.size() + ")";

            String bookName = values.getFirst();
            int pageNumber;
            try {
                pageNumber = Integer.parseInt(values.get(1));
            } catch (NumberFormatException exception) {
                return "Invalid page number: " + values.get(1);
            }
            var bookOpt = textManager.getBook(bookName, player);
            if (bookOpt.isEmpty()) return "Book " + bookName + " not found";

            var book = bookOpt.get();

            var pageOptional = book.getPage(pageNumber);
            if (pageOptional.isEmpty()) return "Page " + pageNumber + " not found";

            return book.toBookString(textManager, plugin.getColorHelper(), pageOptional.get());
        };
    }
}
