package fr.maxlego08.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.records.FontInfo;
import fr.maxlego08.text.api.records.SpecialFontTransformation;
import fr.maxlego08.text.api.utils.ZUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ZTextManager extends ZUtils implements TextManager {

    private final TextPlugin plugin;
    private final List<Alphabet> alphabets = new ArrayList<>();
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
}
