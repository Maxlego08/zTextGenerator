package fr.maxlego08.text.api.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.utils.Alignment;
import org.bukkit.entity.Player;

import java.util.List;

public interface TextLine {

    /**
     * Gets the alignment of this text line.
     *
     * @return the alignment of this text line
     */
    Alignment getAlignment();

    /**
     * Gets the alphabet of the text line.
     *
     * @return the alphabet of the text line
     */
    Alphabet getAlphabet();

    /**
     * Gets the elements of the text line.
     *
     * @return the elements of the text line
     */
    List<TextElement> getElements();
}
