package fr.maxlego08.text.api;

import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.utils.Alignment;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TextManager {

    /**
     * Retrieves the list of alphabets registered in the text manager.
     *
     * @return the list of alphabets registered in the text manager
     */
    List<Alphabet> getAlphabets();

    /**
     * Retrieves the alphabet associated with the given name from the text manager.
     *
     * @param name the name of the alphabet to retrieve
     * @return an optional containing the alphabet associated with the given name, or an empty optional if no such alphabet exists
     */
    Optional<Alphabet> getAlphabet(String name);

    /**
     * Loads all texts from the configuration files.
     */
    void loadTexts();

    /**
     * Loads all texts from the specified file.
     *
     * @param file the file containing the texts to load
     */
    void loadTexts(File file);

    /**
     * Loads all books from the configuration files.
     */
    void loadBooks();

    /**
     * Loads a book from the specified file.
     *
     * @param file the file containing the book to load
     */
    void loadBook(File file);

    /**
     * Loads a text from the given map.
     *
     * @param map the map containing the text information to load
     */
    void loadText(Map<?, ?> map, YamlConfiguration configuration);

    /**
     * Loads all alphabets from the configuration files.
     * This method reads all files in the configuration folder and loads all alphabets
     * from them.
     */
    void loadAlphabets();

    /**
     * Loads an alphabet from the specified file.
     *
     * @param file the file containing the alphabet information to load
     */
    void loadAlphabet(File file);

    /**
     * Retrieves the default offset for all texts in pixels.
     *
     * @return the default offset for all texts in pixels
     */
    String getOffset();

    /**
     * Retrieves the default offset for all texts in pixels, given a specific pixel count.
     *
     * @param pixels the number of pixels to offset
     * @return the default offset for all texts in pixels, given a specific pixel count
     */
    String getOffset(int pixels);

    /**
     * Retrieves the negative offset for all texts in pixels, given a specific pixel count.
     *
     * @param pixels the number of pixels to offset
     * @return the negative offset for all texts in pixels, given a specific pixel count
     */
    String getNegativeOffset(int pixels);

    Alphabet getInventoryTitleAlphabet();

    /**
     * Retrieves the list of texts registered in the text manager.
     *
     * @return the list of texts registered in the text manager
     */
    List<Text> getTexts();

    /**
     * Retrieves a text registered in the text manager by its name.
     *
     * @param name the name of the text to retrieve
     * @return an optional containing the text associated with the given name, or an empty optional if no such text exists
     */
    Optional<Text> getText(String name);

    /**
     * Retrieves a text registered in the text manager by its name and language.
     *
     * @param name     the name of the text to retrieve
     * @param language the preferred language of the text
     * @return an optional containing the text associated with the given name and language, or an empty optional if no such text exists
     */
    Optional<Text> getText(String name, String language);

    /**
     * Retrieves a text registered in the text manager by its name and the player's language.
     *
     * @param name   the name of the text to retrieve
     * @param player the player whose language should be used to resolve the text
     * @return an optional containing the text associated with the given name for the player's language, or an empty optional if no such text exists
     */
    Optional<Text> getText(String name, Player player);

    /**
     * Retrieves the list of books registered in the text manager.
     *
     * @return the list of books registered in the text manager
     */
    List<Book> getBooks();

    /**
     * Retrieves a book registered in the text manager by its name.
     *
     * @param name the name of the book to retrieve
     * @return an optional containing the book associated with the given name, or an empty optional if no such book exists
     */
    Optional<Book> getBook(String name);

    /**
     * Retrieves a book registered in the text manager by its name and language.
     *
     * @param name     the name of the book to retrieve
     * @param language the preferred language of the book
     * @return an optional containing the book associated with the given name and language, or an empty optional if no such book exists
     */
    Optional<Book> getBook(String name, String language);

    /**
     * Retrieves a book registered in the text manager by its name for the provided player's language.
     *
     * @param name   the name of the book to retrieve
     * @param player the player whose language should be used to resolve the book
     * @return an optional containing the book associated with the given name for the player's language, or an empty optional if no such book exists
     */
    Optional<Book> getBook(String name, Player player);

    /**
     * Gets the default language configured for texts and books.
     *
     * @return the default language code, expressed using lowercase language and country codes (for example {@code en_us})
     */
    String getDefaultLanguage();

    /**
     * Replaces all occurrences of special characters in the given content with their respective transformations in the given alphabet.
     *
     * @param alphabet  the alphabet to use for the replacement
     * @param alignment the alignment of the text to replace
     * @param content   the content to replace
     * @param height    the height of the characters to replace
     * @return the replaced content string
     */
    String replaceText(Alphabet alphabet, Alignment alignment, String content, int height);

    /**
     * Replaces all occurrences of special font characters in the given text with their respective transformations in the default alphabet.
     *
     * @param text the text to transform
     * @return the transformed text string
     */
    String transformFont(String text);

    /**
     * Opens a book for the given player at the given page.
     *
     * @param sender   the command sender that requested the book
     * @param player   the player to open the book for
     * @param bookName the name of the book to open
     * @param page     the page of the book to open
     */
    void openBook(CommandSender sender, Player player, String bookName, int page);

    /**
     * Opens a book for the given player at the given page.
     *
     * @param player   the player to open the book for
     * @param book     the book to open
     * @param bookPage the page of the book to open
     */
    void openBook(Player player, Book book, BookPage bookPage);

    /**
     * Displays the specified text to the player using the provided animation options.
     *
     * @param player  the player who should see the text
     * @param text    the text to display
     * @param options the animation options to use when displaying the text
     */
    void displayText(Player player, Text text, TextAnimationOptions options);

    /**
     * Displays the text associated with the provided name to the player using the
     * given animation options.
     *
     * @param player   the player who should see the text
     * @param textName the name of the text to display
     * @param options  the animation options to use when displaying the text
     */
    void displayText(Player player, String textName, TextAnimationOptions options);

    /**
     * Handles the closure of a text inventory by a player. Implementations should ensure
     * any active animation is stopped and the final text is displayed once the inventory
     * is closed for the first time.
     *
     * @param player the player who closed the inventory displaying a text
     */
    void handleTextInventoryClose(Player player);

    /**
     * Processes a list of text lines and returns the resulting string.
     *
     * @param textLines the list of text lines to process
     * @param maxWidth the maximum width of the resulting string
     * @param arguments the arguments to pass to the text line's process method
     * @return the processed string
     */
    String processText(List<TextLine> textLines, int maxWidth, Object... arguments);

    void displayAlphabet(Player player, Alphabet alphabet, String letter, int letterByLine, int maxLines, int letterLength, String inventoryName, int inventorySize);
}
