package fr.maxlego08.text.api.fonts;

public interface FontImage {

    /**
     * Replaces all occurrences of the character represented by this font image in the given string by its image.
     * @param string the string in which to replace the character
     * @return the string with all occurrences of the character replaced by its image
     */
    String replace(String string);

}
