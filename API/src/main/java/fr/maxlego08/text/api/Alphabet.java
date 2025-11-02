package fr.maxlego08.text.api;

import fr.maxlego08.text.api.fonts.FontInfo;

import java.io.File;
import java.util.List;

public interface Alphabet {

    /**
     * Gets the name of this alphabet.
     *
     * @return the name of this alphabet
     */
    String getName();

    /**
     * Gets the file associated with this alphabet.
     *
     * @return the file associated with this alphabet
     */
    File getFile();

    /**
     * Gets the list of font information associated with this alphabet.
     *
     * @return the list of font information associated with this alphabet
     */
    List<FontInfo> getFontInfos();

    /**
     * Gets the font transformation associated with this alphabet.
     *
     * @return the font transformation associated with this alphabet
     */
    FontTransformation getFontTransformation();

    /**
     * Retrieves the length of a given text in this alphabet.
     *
     * @param content the content to retrieve the length of
     * @return the length of the given text in this alphabet
     */
    int getTextLength(String content);

    /**
     * Retrieves the length of a given character in this alphabet.
     *
     * @param c the character to retrieve the length of
     * @return the length of the given character in this alphabet
     */
    int getLength(char c);

    /**
     * Retrieves the transformed character for a given character in this alphabet.
     *
     * @param c      the character to retrieve the transformed character of
     * @param height the height of the transformed character
     * @return the transformed character for the given character in this alphabet
     */
    String transformChar(char c, int height);
}
