package fr.maxlego08.text.api.color;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface ColorHelper {

    String getTextWithoutColor(String text);

    Result transformString(Alphabet alphabet, String string, int height);

    void message(CommandSender sender, String string);

    Inventory createBook(Player player, Book book, BookPage page, TextManager textManager);
}
