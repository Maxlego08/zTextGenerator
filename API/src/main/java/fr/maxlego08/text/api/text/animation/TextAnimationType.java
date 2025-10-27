package fr.maxlego08.text.api.text.animation;

/**
 * Represents the different animation modes supported when displaying a text.
 */
public enum TextAnimationType {

    /**
     * No animation, the full text is displayed immediately.
     */
    NONE,

    /**
     * Reveals the text character by character.
     */
    LETTER_BY_LETTER,

    /**
     * Reveals the text word by word.
     */
    WORD_BY_WORD
}
