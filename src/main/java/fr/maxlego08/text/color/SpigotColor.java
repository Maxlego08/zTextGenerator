package fr.maxlego08.text.color;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;
import fr.maxlego08.text.book.BookInventory;
import fr.maxlego08.text.text.TextInventory;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SpigotColor implements ColorHelper {
    @Override
    public String getTextWithoutColor(String text) {
        return text.replaceAll("(§[0-9A-FK-ORa-fk-or])", "");
    }

    @Override
    public Result transformString(Alphabet alphabet, String string, int height) {
        StringBuilder builder = new StringBuilder();
        int length = 0;
        for (char c : string.toCharArray()) {
            builder.append(alphabet.transformChar(c, height));
            length += alphabet.getLength(c);
        }
        return new Result(builder.toString(), length);
    }

    @Override
    public void message(CommandSender sender, String string) {
        sender.sendMessage(string.replace("&", "§"));
    }

    @Override
    public Inventory createBook(Player player, Book book, BookPage page, TextManager textManager) {
        BookInventory bookInventory = new BookInventory();
        var inventory = Bukkit.createInventory(bookInventory, 54, book.getInventoryName(player) + book.toBookString(textManager, this, page));
        bookInventory.setInventory(inventory);
        return inventory;
    }

    @Override
    public Inventory createTextInventory(Player player, int size, String title) {
        TextInventory textInventory = new TextInventory();
        var inventory = Bukkit.createInventory(textInventory, size, title.replace("&", "§"));
        textInventory.setInventory(inventory);
        return inventory;
    }
}
