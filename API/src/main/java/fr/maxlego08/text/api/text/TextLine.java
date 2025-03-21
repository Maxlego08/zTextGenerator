package fr.maxlego08.text.api.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.utils.Alignment;

import java.util.List;

public interface TextLine {

    Alignment getAlignment();

    Alphabet getAlphabet();

    List<TextElement> getElements();
}
