package fr.maxlego08.text.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.TextLine;
import fr.maxlego08.text.api.utils.Alignment;

import java.util.ArrayList;
import java.util.List;

public class ZText implements Text {

    private final String name;
    private final String title;
    private final Alignment alignment;
    private final Alphabet alphabet;
    private final int length;
    private final List<TextLine> lines;
    private String result;

    public ZText(String name, String title, Alignment alignment, Alphabet alphabet, int length, List<TextLine> lines) {
        this.name = name;
        this.title = title;
        this.alignment = alignment;
        this.alphabet = alphabet;
        this.length = length;
        this.lines = lines;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    @Override
    public int getLength() {
        return length;
    }

    @Override
    public List<TextLine> getLines() {
        return lines;
    }

    @Override
    public String getResult() {
        return this.result;
    }

    @Override
    public void createResult() {

    }

    @Override
    public boolean hasResult() {
        return this.result != null;
    }
}
