package fr.maxlego08.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.book.PageContent;
import fr.maxlego08.text.api.book.PageType;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.fonts.FontInfo;
import fr.maxlego08.text.api.fonts.SpecialFontTransformation;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextElement;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.Alignment;
import fr.maxlego08.text.api.utils.ZUtils;
import fr.maxlego08.text.book.ZBook;
import fr.maxlego08.text.text.ZText;
import fr.maxlego08.text.text.ZTextLine;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ZTextManager extends ZUtils implements TextManager {

    private final TextPlugin plugin;
    private final List<Alphabet> alphabets = new ArrayList<>();
    private final List<Text> texts = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private String offset;

    public ZTextManager(TextPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<Alphabet> getAlphabets() {
        return this.alphabets;
    }

    @Override
    public Optional<Alphabet> getAlphabet(String name) {
        return this.alphabets.stream().filter(alphabet -> alphabet.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public void loadBooks() {

        this.books.clear();

        File folder = new File(this.plugin.getDataFolder(), "books");
        if (!folder.exists()) {
            folder.mkdirs();

            this.plugin.saveResource("books/book-example.yml", false);
        }

        this.files(folder, this::loadBook);
        this.plugin.getLogger().info("Loaded " + this.books.size() + " books");
    }

    @Override
    public void loadBook(File file) {
        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

        String name = configuration.getString("name");
        String inventoryName = configuration.getString("inventory-name");
        String alphabetName = configuration.getString("alphabet");
        Alignment alignment = Alignment.valueOf(configuration.getString("alignment", Alignment.LEFT.name()));
        int startOffset = configuration.getInt("start-offset");
        int leftSize = configuration.getInt("left-size");
        int rightSize = configuration.getInt("right-size");
        int offsetBetween = configuration.getInt("offset-between");
        List<Map<?, ?>> pages = configuration.getMapList("pages");
        Optional<Alphabet> optional = this.getAlphabet(alphabetName);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Alphabet " + alphabetName + " not found");
        }
        Alphabet alphabet = optional.get();

        List<BookPage> bookPages = new ArrayList<>();
        pages.forEach(map -> {
            var left = map.get("left");
            var right = map.get("right");
            int page = Integer.parseInt(map.get("page").toString());
            bookPages.add(new BookPage(page, loadPage(right, alphabet, alignment), loadPage(left, alphabet, alignment)));
        });


        this.books.add(new ZBook(name, inventoryName, bookPages, startOffset, leftSize, rightSize, offsetBetween, alphabet));
    }

    private Page loadPage(Object object, Alphabet alphabet, Alignment alignment) {

        if (object instanceof String string) {
            return new Page(PageType.FILL, List.of(new PageContent(alignment, alphabet, string)));
        } else if (object instanceof List<?> list) {

            List<PageContent> pageContents = new ArrayList<>();
            for (Object o : list) {

                if (o instanceof Map<?, ?> map) {
                    Alignment pageAlignment = map.containsKey("alignment") ? Alignment.valueOf(map.get("alignment").toString()) : alignment;
                    if (map.containsKey("alphabet")) {
                        String alphabetName = map.get("alphabet").toString();
                        Optional<Alphabet> optional = this.getAlphabet(alphabetName);
                        if (optional.isEmpty()) {
                            throw new IllegalArgumentException("Alphabet " + alphabetName + " not found");
                        }
                        alphabet = optional.get();
                    }

                    String content = map.get("content").toString();
                    pageContents.add(new PageContent(pageAlignment, alphabet, content));
                } else {
                    throw new IllegalArgumentException("Invalid page for " + object);
                }
            }

            return new Page(PageType.LINE, pageContents);

        } else {
            throw new IllegalArgumentException("Invalid page for " + object);
        }
    }

    @Override
    public void loadTexts() {

        File folder = new File(this.plugin.getDataFolder(), "texts");
        if (!folder.exists()) {
            folder.mkdirs();

            this.plugin.saveResource("texts/text-example.yml", false);
        }

        this.files(folder, this::loadTexts);
    }

    @Override
    public void loadTexts(File file) {

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        List<Map<?, ?>> maps = configuration.getMapList("texts");
        int before = this.texts.size();
        maps.forEach(this::loadText);

        this.plugin.getLogger().info("Loaded " + (this.texts.size() - before) + " texts from " + file.getName());
    }

    @Override
    public void loadText(Map<?, ?> map) {

        if (!map.containsKey("name")) {
            throw new IllegalArgumentException("Text must have a name.");
        }

        if (!map.containsKey("lines")) {
            throw new IllegalArgumentException("Text must have lines.");
        }

        if (!map.containsKey("alphabet")) {
            throw new IllegalArgumentException("Text must have an alphabet.");
        }

        String name = (String) map.get("name");
        this.texts.removeIf(text -> text.getName().equalsIgnoreCase(name));

        String title = map.containsKey("title") ? (String) map.get("title") : null;
        Alignment alignment = map.containsKey("alignment") ? Alignment.valueOf((String) map.get("alignment")) : Alignment.LEFT;

        String alphabetName = (String) map.get("alphabet");
        var optional = this.getAlphabet(alphabetName);
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("Alphabet " + alphabetName + " not found for text " + name);
        }
        Alphabet alphabet = optional.get();

        int length = map.containsKey("length") ? ((Number) map.get("length")).intValue() : 200;

        List<?> lines = (List<?>) map.get("lines");
        List<TextLine> textLines = new ArrayList<>();
        for (Object line : lines) {

            System.out.println(line);

            if (line instanceof String string) {

                textLines.add(new ZTextLine(string));
            } else if (line instanceof Map<?, ?> lineMap) {

                Alignment ligneAlignment = lineMap.containsKey("alignment") ? Alignment.valueOf((String) lineMap.get("alignment")) : null;
                Alphabet ligneAlphabet = null;
                if (lineMap.containsKey("alphabet")) {
                    alphabetName = (String) lineMap.get("alphabet");
                    optional = this.getAlphabet(alphabetName);
                    if (optional.isPresent()) {
                        ligneAlphabet = optional.get();
                    }
                }

                if (!lineMap.containsKey("elements")) {
                    throw new IllegalArgumentException("Text line must have an elements.");
                }

                List<TextElement> textElements = new ArrayList<>();

                List<Map<?, ?>> elements = (List<Map<?, ?>>) lineMap.get("elements");
                for (Map<?, ?> elementMap : elements) {
                    Alphabet elementAlphabet = null;
                    if (elementMap.containsKey("alphabet")) {
                        alphabetName = (String) elementMap.get("alphabet");
                        optional = this.getAlphabet(alphabetName);
                        if (optional.isPresent()) {
                            elementAlphabet = optional.get();
                        }
                    }

                    String element = (String) elementMap.get("element");
                    textElements.add(new TextElement(elementAlphabet, element));
                }

                textLines.add(new ZTextLine(ligneAlphabet, ligneAlignment, textElements));
            }
        }

        Text text = new ZText(this.plugin, name, title, alignment, alphabet, length, textLines);
        text.createResult();

        this.texts.add(text);
    }

    @Override
    public void loadAlphabets() {
        File folder = new File(this.plugin.getDataFolder(), "alphabets");
        if (!folder.exists()) {
            folder.mkdirs();

            this.plugin.saveResource("alphabets/normal.yml", false);
            this.plugin.saveResource("alphabets/inventory-title.yml", false);
        }

        this.files(folder, this::loadAlphabet);

        this.offset = this.plugin.getConfig().getString("offset", ":offset-%pixels%:");
    }

    @Override
    public void loadAlphabet(File file) {

        YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        String name = configuration.getString("name");

        this.alphabets.removeIf(alphabet -> alphabet.getName().equalsIgnoreCase(name));

        List<FontInfo> fontInfos = this.loadFontInfo(configuration);
        if (fontInfos.isEmpty()) {
            this.plugin.getLogger().severe("No font infos found for alphabet " + name + " from " + file.getName());
        }

        List<SpecialFontTransformation> specialFontTransformations = this.loadFontTransformations(configuration);
        String upperCase = configuration.getString("font-transformations.upper-case");
        String lowerCase = configuration.getString("font-transformations.lower-case");

        FontTransformation fontTransformation = new ZFontTransformation(upperCase, lowerCase, specialFontTransformations);

        this.alphabets.add(new ZAlphabet(this.plugin, name, file, fontInfos, fontTransformation));
        this.plugin.getLogger().info("Loaded alphabet " + name + " from " + file.getName());
    }

    @Override
    public String getOffset() {
        return this.offset;
    }

    @Override
    public String getOffset(int pixels) {
        return pixels == 0 ? "" : this.offset.replace("%pixels%", String.valueOf(pixels));
    }

    @Override
    public String getNegativeOffset(int pixels) {
        return getOffset(-pixels);
    }

    private List<FontInfo> loadFontInfo(YamlConfiguration configuration) {

        List<FontInfo> fontInfos = new ArrayList<>();
        List<Map<?, ?>> maps = configuration.getMapList("font-infos");

        for (Map<?, ?> map : maps) {
            try {

                char character = ((String) map.get("char")).charAt(0);
                int length = ((Number) map.get("length")).intValue();
                fontInfos.add(new FontInfo(character, length));

            } catch (Exception exception) {
                this.plugin.getLogger().severe("Error while loading font infos");
                exception.printStackTrace();
            }
        }

        return fontInfos;
    }

    private List<SpecialFontTransformation> loadFontTransformations(YamlConfiguration configuration) {

        List<SpecialFontTransformation> specialFontTransformations = new ArrayList<>();
        List<Map<?, ?>> maps = configuration.getMapList("font-transformations.specials");

        for (Map<?, ?> map : maps) {
            try {

                char character = ((String) map.get("char")).charAt(0);
                String replacement = (String) map.get("replacement");
                specialFontTransformations.add(new SpecialFontTransformation(character, replacement));

            } catch (Exception exception) {
                this.plugin.getLogger().severe("Error while loading font transformations");
                exception.printStackTrace();
            }
        }

        return specialFontTransformations;
    }

    @Override
    public Alphabet getInventoryTitleAlphabet() {
        var optional = getAlphabet("inventory-title");
        if (optional.isEmpty()) {
            throw new RuntimeException("Inventory title alphabet not found");
        }
        return optional.get();
    }

    @Override
    public List<Text> getTexts() {
        return this.texts;
    }

    @Override
    public Optional<Text> getText(String name) {
        return this.texts.stream().filter(text -> text.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public Optional<Book> getBook(String name) {
        return this.books.stream().filter(book -> book.getName().equalsIgnoreCase(name)).findFirst();
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String replaceText(Alphabet alphabet, Alignment alignment, String content, int height) {
        var result = this.plugin.getColorHelper().transformString(alphabet, content, height);
        var textLength = result.length();

        StringBuilder sb = new StringBuilder();

        switch (alignment) {
            case CENTER -> {
                int length = textLength / 2;
                int rest = textLength % 2;
                sb.append(this.getNegativeOffset(length + rest));
                sb.append(result.string());
                sb.append(this.getNegativeOffset(textLength + rest));
            }
            case LEFT -> {
                sb.append(result.string());
                sb.append(this.getNegativeOffset(textLength));
            }
            case RIGHT -> {
                sb.append(this.getNegativeOffset(textLength));
                sb.append(result.string());
            }
        }

        return this.transformFont(sb.toString());
    }

    @Override
    public String transformFont(String text) {
        return this.plugin.getFontImage().replace(text).replace("§f", "").replace("§r", "");
    }

    @Override
    public void openBook(CommandSender sender, Player player, String bookName, int page) {

        var optional = getBook(bookName);
        if (optional.isEmpty()) {
            message(plugin, sender, Message.BOOK_NOT_FOUND, "%book%", bookName);
            return;
        }

        var book = optional.get();
        var optionalBookPage = book.getPages().stream().filter(bookPage -> bookPage.page() == page).findFirst();
        if (optionalBookPage.isEmpty()) {
            message(plugin, sender, Message.BOOK_PAGE_NOT_FOUND, "%page%", page);
            return;
        }

        var bookPage = optionalBookPage.get();
        openBook(player, book, bookPage);
    }

    @Override
    public void openBook(Player player, Book book, BookPage bookPage) {
        var inventory = this.plugin.getColorHelper().createBook(player, book, bookPage, this);
        player.openInventory(inventory);
    }
}
