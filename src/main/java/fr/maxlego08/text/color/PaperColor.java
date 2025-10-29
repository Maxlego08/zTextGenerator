package fr.maxlego08.text.color;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.color.Result;
import fr.maxlego08.text.book.BookInventory;
import fr.maxlego08.text.text.TextInventory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaperColor implements ColorHelper {

    private final MiniMessage MINI_MESSAGE = MiniMessage.builder().tags(TagResolver.builder().resolver(StandardTags.defaults()).build()).build();
    private final Map<String, String> COLORS_MAPPINGS = new HashMap<>();

    public PaperColor() {
        this.COLORS_MAPPINGS.put("0", "black");
        this.COLORS_MAPPINGS.put("1", "dark_blue");
        this.COLORS_MAPPINGS.put("2", "dark_green");
        this.COLORS_MAPPINGS.put("3", "dark_aqua");
        this.COLORS_MAPPINGS.put("4", "dark_red");
        this.COLORS_MAPPINGS.put("5", "dark_purple");
        this.COLORS_MAPPINGS.put("6", "gold");
        this.COLORS_MAPPINGS.put("7", "gray");
        this.COLORS_MAPPINGS.put("8", "dark_gray");
        this.COLORS_MAPPINGS.put("9", "blue");
        this.COLORS_MAPPINGS.put("a", "green");
        this.COLORS_MAPPINGS.put("b", "aqua");
        this.COLORS_MAPPINGS.put("c", "red");
        this.COLORS_MAPPINGS.put("d", "light_purple");
        this.COLORS_MAPPINGS.put("e", "yellow");
        this.COLORS_MAPPINGS.put("f", "white");
        this.COLORS_MAPPINGS.put("k", "obfuscated");
        this.COLORS_MAPPINGS.put("l", "bold");
        this.COLORS_MAPPINGS.put("m", "strikethrough");
        this.COLORS_MAPPINGS.put("n", "underlined");
        this.COLORS_MAPPINGS.put("o", "italic");
        this.COLORS_MAPPINGS.put("r", "reset");
    }

    private String colorMiniMessage(String message) {

        String newMessage = message;

        Pattern pattern = Pattern.compile("(?<!<)(?<!:)#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            newMessage = newMessage.replace(color, "<" + color + ">");
            message = message.replace(color, "");
            matcher = pattern.matcher(message);
        }

        for (Map.Entry<String, String> entry : this.COLORS_MAPPINGS.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            newMessage = newMessage.replace("&" + key, "<" + value + ">");
            newMessage = newMessage.replace("§" + key, "<" + value + ">");
            newMessage = newMessage.replace("&" + key.toUpperCase(), "<" + value + ">");
            newMessage = newMessage.replace("§" + key.toUpperCase(), "<" + value + ">");
        }

        return newMessage;
    }

    public Component getComponent(String text) {
        return this.MINI_MESSAGE.deserialize(colorMiniMessage(text));
    }

    @Override
    public String getTextWithoutColor(String text) {
        return PlainTextComponentSerializer.plainText().serialize(getComponent(text));
    }

    @Override
    public Result transformString(Alphabet alphabet, String string, int height) {

        string = colorMiniMessage(string);

        boolean insideTag = false;
        StringBuilder tagBuilder = new StringBuilder();
        StringBuilder finalResult = new StringBuilder();
        int length = 0;

        for (char c : string.toCharArray()) {
            if (c == '<') {
                insideTag = true;
                tagBuilder.append(c);
                continue;
            }

            if (insideTag) {
                tagBuilder.append(c);
                if (c == '>') {
                    finalResult.append(tagBuilder);
                    tagBuilder.setLength(0);
                    insideTag = false;
                }
                continue;
            }

            length += alphabet.getLength(c);
            finalResult.append(alphabet.transformChar(c, height));
        }

        return new Result(finalResult.toString(), length);
    }

    @Override
    public void message(CommandSender sender, String string) {
        sender.sendMessage(getComponent(string));
    }

    @Override
    public Inventory createBook(Player player, Book book, BookPage page, TextManager textManager) {
        BookInventory bookInventory = new BookInventory();
        var inventory = Bukkit.createInventory(bookInventory, 54, getComponent(book.getInventoryName() + book.toBookString(textManager, this, page)));
        bookInventory.setInventory(inventory);
        return inventory;
    }

    @Override
    public Inventory createTextInventory(Player player, String title) {
        TextInventory textInventory = new TextInventory();
        System.out.println("TITLE : " + title);
        var inventory = Bukkit.createInventory(textInventory, 9, getComponent(title));
        textInventory.setInventory(inventory);
        return inventory;
    }
}
