package fr.maxlego08.text.api.color;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ColorHelper {

    /**
     * Gets a string without any color code.
     *
     * @param text The string to transform.
     * @return The string without any color code.
     */
    String getTextWithoutColor(String text);

    /**
     * Transforms a string by applying the given alphabet and height.
     *
     * @param alphabet The alphabet to use for the transformation.
     * @param string   The string to transform.
     * @param height   The height of the transformed string.
     * @return The transformed string along with its length.
     */
    Result transformString(Alphabet alphabet, String string, int height);

    /**
     * Sends a message to a player.
     *
     * @param sender The sender of the message.
     * @param string The message to send.
     */
    void message(CommandSender sender, String string);

    /**
     * Creates a book containing the given page.
     *
     * @param player      The player to create the book for.
     * @param book        The book to create.
     * @param page        The page to add to the book.
     * @param textManager The text manager to use for the page.
     * @return The inventory of the created book.
     */
    Inventory createBook(Player player, Book book, BookPage page, TextManager textManager);

    /**
     * Creates an inventory used to display a generated text to a player.
     *
     * @param player The player who will view the inventory.
     * @param title  The inventory title to display.
     * @return The created inventory.
     */
    Inventory createTextInventory(Player player, String title);
}
