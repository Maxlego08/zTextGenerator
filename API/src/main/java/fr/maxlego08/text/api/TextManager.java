package fr.maxlego08.text.api;

import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.utils.Alignment;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TextManager {

    List<Alphabet> getAlphabets();

    Optional<Alphabet> getAlphabet(String name);

    void loadTexts();

    void loadTexts(File file);

    void loadBooks();

    void loadBook(File file);

    void loadText(Map<?, ?> map);

    void loadAlphabets();

    void loadAlphabet(File file);

    String getOffset();

    String getOffset(int pixels);

    String getNegativeOffset(int pixels);

    Alphabet getInventoryTitleAlphabet();

    List<Text> getTexts();

    Optional<Text> getText(String name);

    List<Book> getBooks();

    Optional<Book> getBook(String name);

    String replaceText(Alphabet alphabet, Alignment alignment, String content, int height);

    String transformFont(String text);

    void openBook(CommandSender sender, Player player, String bookName, int page);

    void openBook(Player player, Book book, BookPage bookPage);
}
