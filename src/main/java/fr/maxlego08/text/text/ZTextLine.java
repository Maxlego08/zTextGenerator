package fr.maxlego08.text.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.text.TextElement;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.Alignment;
import org.bukkit.entity.Player;

import java.util.List;

public class ZTextLine implements TextLine {

    private final Alphabet alphabet;
    private final Alignment alignment;
    private final List<TextElement> elements;

    public ZTextLine(Alphabet alphabet, Alignment alignment, List<TextElement> elements) {
        this.alphabet = alphabet;
        this.alignment = alignment;
        this.elements = elements;
    }

    public ZTextLine(String string) {
        this.alphabet = null;
        this.alignment = null;
        this.elements = List.of(new TextElement(null, string));
    }

    @Override
    public Alignment getAlignment() {
        return this.alignment;
    }

    @Override
    public Alphabet getAlphabet() {
        return this.alphabet;
    }

    @Override
    public List<TextElement> getElements() {
        return this.elements;
    }
}
