package fr.maxlego08.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.book.Page;
import fr.maxlego08.text.api.book.PageType;
import fr.maxlego08.text.api.fonts.FontInfo;
import fr.maxlego08.text.api.fonts.SpecialFontTransformation;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.utils.Alignment;
import fr.maxlego08.text.api.utils.ZUtils;
import fr.maxlego08.text.book.ZBook;
import fr.maxlego08.text.text.ZText;
import fr.maxlego08.text.text.animation.TextAnimationTask;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ZTextManager extends ZUtils implements TextManager {

    private static final String ENGLISH_LANGUAGE = "en_us";

    private final TextPlugin plugin;
    private final List<Alphabet> alphabets = new ArrayList<>();
    private final List<Text> texts = new ArrayList<>();
    private final List<Book> books = new ArrayList<>();
    private final Map<UUID, TextAnimationTask> activeTextAnimations = new ConcurrentHashMap<>();
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
        String language = normalizeLanguage(configuration.getString("language", this.getDefaultLanguage()));
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
        this.books.removeIf(book -> book.getName().equalsIgnoreCase(name) && book.getLanguage().equalsIgnoreCase(language));
        this.books.add(new ZBook(name, language, inventoryName, bookPages, startOffset, leftSize, rightSize, offsetBetween, alphabet));
    }

    private Page loadPage(Object object, Alphabet alphabet, Alignment alignment) {

        if (object instanceof String string) {
            return new Page(PageType.FILL, List.of(new TextLine(alignment, alphabet, string)));
        } else if (object instanceof List<?> list) {

            List<TextLine> pageContents = new ArrayList<>();
            for (Object o : list) {

                if (o instanceof Map<?, ?> map) {
                    Alphabet lineAlphabet = alphabet;
                    Alignment pageAlignment = map.containsKey("alignment") ? Alignment.valueOf(map.get("alignment").toString()) : alignment;
                    if (map.containsKey("alphabet")) {
                        String alphabetName = map.get("alphabet").toString();
                        Optional<Alphabet> optional = this.getAlphabet(alphabetName);
                        if (optional.isEmpty()) {
                            throw new IllegalArgumentException("Alphabet " + alphabetName + " not found");
                        }
                        lineAlphabet = optional.get();
                    }

                    String content = map.get("content").toString();
                    pageContents.add(new TextLine(pageAlignment, lineAlphabet, content));
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
        String language = normalizeLanguage(map.containsKey("language") ? map.get("language").toString() : this.getDefaultLanguage());
        this.texts.removeIf(text -> text.getName().equalsIgnoreCase(name) && text.getLanguage().equalsIgnoreCase(language));

        String title = map.containsKey("title") ? (String) map.get("title") : null;
        Alignment alignment = map.containsKey("alignment") ? Alignment.valueOf(((String) map.get("alignment")).toUpperCase()) : Alignment.LEFT;

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

            if (line instanceof String string) {

                textLines.add(new TextLine(alignment, alphabet, string));
            } else if (line instanceof Map<?, ?> lineMap) {

                Alignment ligneAlignment = lineMap.containsKey("alignment") ? Alignment.valueOf(((String) lineMap.get("alignment")).toUpperCase()) : alignment;
                Alphabet ligneAlphabet = alphabet;
                if (lineMap.containsKey("alphabet")) {
                    alphabetName = (String) lineMap.get("alphabet");
                    optional = this.getAlphabet(alphabetName);
                    if (optional.isPresent()) {
                        ligneAlphabet = optional.get();
                    }
                }

                String element = (String) lineMap.get("element");
                textLines.add(new TextLine(ligneAlignment, ligneAlphabet, element));
            }
        }

        Text text = new ZText(this.plugin, name, language, title, length, textLines);
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
        return this.getText(name, this.getDefaultLanguage());
    }

    @Override
    public Optional<Text> getText(String name, String language) {
        return findText(name, normalizeLanguage(language));
    }

    @Override
    public Optional<Text> getText(String name, Player player) {
        return findText(name, resolveLanguage(player));
    }

    @Override
    public Optional<Book> getBook(String name) {
        return this.getBook(name, this.getDefaultLanguage());
    }

    @Override
    public Optional<Book> getBook(String name, String language) {
        return findBook(name, normalizeLanguage(language));
    }

    @Override
    public Optional<Book> getBook(String name, Player player) {
        return findBook(name, resolveLanguage(player));
    }

    @Override
    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String getDefaultLanguage() {
        return this.plugin.getDefaultLanguage();
    }

    private Optional<Text> findText(String name, String language) {
        String normalized = language == null || language.isEmpty() ? this.getDefaultLanguage() : language;

        Optional<Text> optional = matchText(name, normalized);
        if (optional.isPresent()) {
            return optional;
        }

        if (!normalized.equalsIgnoreCase(ENGLISH_LANGUAGE)) {
            optional = matchText(name, ENGLISH_LANGUAGE);
            if (optional.isPresent()) {
                return optional;
            }
        }

        String defaultLanguage = this.getDefaultLanguage();
        if (!normalized.equalsIgnoreCase(defaultLanguage) && !defaultLanguage.equalsIgnoreCase(ENGLISH_LANGUAGE)) {
            optional = matchText(name, defaultLanguage);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return this.texts.stream().filter(text -> text.getName().equalsIgnoreCase(name)).findFirst();
    }

    private Optional<Text> matchText(String name, String language) {
        return this.texts.stream()
                .filter(text -> text.getName().equalsIgnoreCase(name) && text.getLanguage().equalsIgnoreCase(language))
                .findFirst();
    }

    private Optional<Book> findBook(String name, String language) {
        String normalized = language == null || language.isEmpty() ? this.getDefaultLanguage() : language;

        Optional<Book> optional = matchBook(name, normalized);
        if (optional.isPresent()) {
            return optional;
        }

        if (!normalized.equalsIgnoreCase(ENGLISH_LANGUAGE)) {
            optional = matchBook(name, ENGLISH_LANGUAGE);
            if (optional.isPresent()) {
                return optional;
            }
        }

        String defaultLanguage = this.getDefaultLanguage();
        if (!normalized.equalsIgnoreCase(defaultLanguage) && !defaultLanguage.equalsIgnoreCase(ENGLISH_LANGUAGE)) {
            optional = matchBook(name, defaultLanguage);
            if (optional.isPresent()) {
                return optional;
            }
        }

        return this.books.stream().filter(book -> book.getName().equalsIgnoreCase(name)).findFirst();
    }

    private Optional<Book> matchBook(String name, String language) {
        return this.books.stream()
                .filter(book -> book.getName().equalsIgnoreCase(name) && book.getLanguage().equalsIgnoreCase(language))
                .findFirst();
    }

    private String resolveLanguage(Player player) {
        if (player == null) {
            return this.getDefaultLanguage();
        }

        try {
            Locale locale = player.locale();
            if (locale != null) {
                String language = locale.toString();
                if (language == null || language.isEmpty()) {
                    language = locale.toLanguageTag();
                }
                if (language != null && !language.isEmpty()) {
                    return normalizeLanguage(language);
                }
            }
        } catch (NoSuchMethodError ignored) {
            // Fallback to legacy API
        }

        try {
            Method legacyMethod = Player.class.getMethod("getLocale");
            Object legacy = legacyMethod.invoke(player);
            if (legacy != null) {
                String legacyLocale = legacy.toString();
                if (!legacyLocale.isEmpty()) {
                    return normalizeLanguage(legacyLocale);
                }
            }
        } catch (ReflectiveOperationException ignored) {
            // Method not available or failed to invoke
        } catch (NoSuchMethodError ignored) {
            // API not available on this version
        }

        return this.getDefaultLanguage();
    }

    private String normalizeLanguage(String language) {
        if (language == null || language.isEmpty()) {
            return this.getDefaultLanguage();
        }
        return language.replace('-', '_').toLowerCase(Locale.ROOT);
    }

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

        var optional = getBook(bookName, player);
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

    @Override
    public void displayText(Player player, Text text, TextAnimationOptions options) {

        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null when displaying a text.");
        }

        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null when displaying it to a player.");
        }

        TextAnimationOptions effectiveOptions = options == null ? TextAnimationOptions.none() : options;
        String renderedText = text.getResult(player);

        TextAnimationTask task = new TextAnimationTask(this.plugin, this, player, renderedText, effectiveOptions);
        if (task.isAnimated()) {
            this.registerAnimation(player, task);
        } else {
            this.clearAnimation(player);
        }
        task.start();
    }

    @Override
    public void displayText(Player player, String textName, TextAnimationOptions options) {

        if (textName == null || textName.isEmpty()) {
            throw new IllegalArgumentException("Text name cannot be null or empty when displaying it to a player.");
        }

        var optional = this.getText(textName, player);
        if (optional.isEmpty()) {
            this.plugin.getLogger().warning("Unable to display text '" + textName + "' because it does not exist.");
            return;
        }

        this.displayText(player, optional.get(), options);
    }

    @Override
    public void handleTextInventoryClose(Player player) {
        if (player == null) {
            return;
        }

        UUID uuid = player.getUniqueId();
        TextAnimationTask task = this.activeTextAnimations.remove(uuid);
        if (task != null) {
            task.stopAndShowFinal();
        }
    }

    @Override
    public String processText(List<TextLine> textLines, int maxWidth, Object... arguments) {

        var colorHelper = this.plugin.getColorHelper();

        StringBuilder builder = new StringBuilder();
        int height = 0;
        boolean hasArguments = arguments.length != 0;

        for (TextLine textLine : textLines) {

            var text = hasArguments ? parseContent(textLine.content(), arguments) : textLine.content();
            var alignment = textLine.alignment();
            var currentAlphabet = textLine.alphabet();

            var result = colorHelper.transformString(currentAlphabet, text, height);
            var content = this.plugin.getFontImage().replace(result.string());

            switch (alignment) {
                case CENTER -> {
                    int centerWidth = maxWidth / 2;
                    int length = result.length() / 2;
                    int rest = result.length() % 2;

                    int start = centerWidth - (length + rest);

                    builder.append(this.getOffset(start));
                    builder.append(content);
                    builder.append(this.getNegativeOffset(start + result.length()));
                }
                case LEFT -> {
                    builder.append(content);
                    builder.append(this.getNegativeOffset(result.length()));
                }
                case RIGHT -> {
                    builder.append(this.getOffset(maxWidth - result.length()));
                    builder.append(content);
                    builder.append(this.getNegativeOffset(maxWidth));
                }
            }
            height += 1;
        }

        return builder.toString();
    }

    public void onAnimationStopped(UUID uuid, TextAnimationTask task) {
        this.activeTextAnimations.compute(uuid, (key, current) -> current == task ? null : current);
    }

    private void registerAnimation(Player player, TextAnimationTask task) {
        UUID uuid = player.getUniqueId();
        TextAnimationTask previous = this.activeTextAnimations.put(uuid, task);
        if (previous != null && previous != task) {
            previous.cancelAnimation();
        }
    }

    private void clearAnimation(Player player) {
        UUID uuid = player.getUniqueId();
        TextAnimationTask previous = this.activeTextAnimations.remove(uuid);
        if (previous != null) {
            previous.cancelAnimation();
        }
    }
}
