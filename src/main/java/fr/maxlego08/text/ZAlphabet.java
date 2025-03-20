package fr.maxlego08.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.records.FontInfo;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class ZAlphabet implements Alphabet {

    private final Plugin plugin;
    private final String name;
    private final File file;
    private final List<FontInfo> fontInfos;
    private final FontTransformation fontTransformation;

    public ZAlphabet(Plugin plugin, String name, File file, List<FontInfo> fontInfos, FontTransformation fontTransformation) {
        this.plugin = plugin;
        this.name = name;
        this.file = file;
        this.fontInfos = fontInfos;
        this.fontTransformation = fontTransformation;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public List<FontInfo> getFontInfos() {
        return this.fontInfos;
    }

    @Override
    public FontTransformation getFontTransformation() {
        return this.fontTransformation;
    }

    @Override
    public int getTextLength(String content) {
        int length = 0;
        for (char c : content.toCharArray()) {
            Optional<FontInfo> fontInfoOptional = this.fontInfos.stream().filter(e -> e.character() == c).findFirst();
            length += fontInfoOptional.map(FontInfo::length).orElseGet(() -> {
                this.plugin.getLogger().info("Unknown character: " + c + " for alphabet: " + this.name);
                return 0;
            });
        }
        return length;
    }

    @Override
    public String transformChar(char c, int height) {
        String result = this.fontTransformation.transformChar(c, height);
        if (result != null) return result;

        this.plugin.getLogger().info("Unknown character: " + c + " for alphabet: " + this.name);
        return " ";
    }
}
