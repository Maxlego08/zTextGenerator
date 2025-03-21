package fr.maxlego08.text.api.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.utils.Alignment;

import java.util.List;

public interface Text {
    String getName();

    String getTitle();

    Alignment getAlignment();

    Alphabet getAlphabet();

    int getLength();

    List<TextLine> getLines();

    String getResult();

    void createResult();

    boolean hasResult();

}


