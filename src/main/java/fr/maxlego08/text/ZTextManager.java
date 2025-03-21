package fr.maxlego08.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.records.FontInfo;
import fr.maxlego08.text.api.records.SpecialFontTransformation;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.Alignment;
import fr.maxlego08.text.api.utils.ZUtils;
import fr.maxlego08.text.text.ZText;
import fr.maxlego08.text.text.ZTextLine;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ZTextManager extends ZUtils implements TextManager {

    private final TextPlugin plugin;
    private final List<Alphabet> alphabets = new ArrayList<>();
    private final List<Text> texts = new ArrayList<>();
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
            if (line instanceof String string) {
                textLines.add(new ZTextLine(string));
            }
        }

        Text text = new ZText(name, title, alignment, alphabet, length, textLines);
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
        return this.offset.replace("%pixels%", String.valueOf(pixels));
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
}
