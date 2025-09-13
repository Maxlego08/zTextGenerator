package fr.maxlego08.text.api.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.color.ColorHelper;

import java.util.List;
import java.util.Optional;

public interface Book {

    /**
     * Get the name of the book.
     *
     * @return The name of the book.
     */
    String getName();

    /**
     * Get the name of the book as it appears in the inventory.
     *
     * @return The name of the book as it appears in the inventory.
     */
    String getInventoryName();

    /**
     * Get the list of all pages in the book.
     *
     * @return The list of all pages in the book.
     */
    List<BookPage> getPages();

    /**
     * Get the offset of the first page of the book in the inventory.
     *
     * @return The offset of the first page of the book in the inventory.
     */
    int getStartOffset();

    /**
     * Get the size of the left page of the book in the inventory.
     *
     * @return The size of the left page of the book in the inventory.
     */
    int getLeftSize();

    /**
     * Get the size of the right page of the book in the inventory.
     *
     * @return The size of the right page of the book in the inventory.
     */
    int getRightSize();

    /**
     * Get the offset between the left and right pages of the book in the inventory.
     *
     * @return The offset between the left and right pages of the book in the inventory.
     */
    int getOffsetBetween();

    /**
     * Get the alphabet used in the book.
     *
     * @return The alphabet used in the book.
     */
    Alphabet getAlphabet();

    /**
     * Get the page of the book at the given index.
     *
     * @param page The index of the page to get.
     * @return The page of the book at the given index, or an empty Optional if the page index is out of bounds.
     */
    Optional<BookPage> getPage(int page);

    /**
     * Converts the given page to a string that can be displayed in the
     * book. The string is formatted according to the given text manager
     * and color helper.
     *
     * @param textManager The text manager to use for formatting the
     *                    string.
     * @param colorHelper The color helper to use for formatting the string.
     * @param page        The page to convert to a string.
     * @return The formatted string.
     */
    String toBookString(TextManager textManager, ColorHelper colorHelper, BookPage page);

}
