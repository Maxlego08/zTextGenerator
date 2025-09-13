package fr.maxlego08.text.api;

import fr.maxlego08.text.api.fonts.SpecialFontTransformation;

import java.util.List;
import java.util.Optional;

public interface FontTransformation {

    /**
     * Gets the list of special font transformations associated with this alphabet.
     *
     * @return the list of special font transformations associated with this alphabet
     */
    List<SpecialFontTransformation> getFontTransformations();

    /**
     * Retrieves the string associated with the upper case character of this alphabet.
     *
     * @return the string associated with the upper case character of this alphabet
     */
    String getUpperCase();

    /**
     * Retrieves the string associated with the lower case character of this alphabet.
     *
     * @return the string associated with the lower case character of this alphabet
     */
    String getLowerCase();

    /**
     * Retrieves the transformed character for a given character in this alphabet.
     *
     * @param c      the character to retrieve the transformed character of
     * @param height the height of the transformed character
     * @return the transformed character for the given character in this alphabet
     */
    String transformChar(char c, int height);

    /**
     * Retrieves the special font transformation associated with the given character in this alphabet.
     *
     * @param c the character to retrieve the special font transformation of
     * @return an optional containing the special font transformation associated with the given character, or an empty optional if no such transformation exists
     */
    Optional<SpecialFontTransformation> getFontTransformation(char c);

}
