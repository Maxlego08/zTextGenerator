package fr.maxlego08.text.animation;

import java.util.Locale;

public enum AnimationMode {

    NONE,
    LETTER_BY_LETTER,
    WORD_BY_WORD;

    public static AnimationMode fromString(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase(Locale.ROOT).replace('-', '_');
        for (AnimationMode mode : values()) {
            if (mode.name().equals(normalized)) {
                return mode;
            }
        }
        return null;
    }

    public boolean requiresAnimation() {
        return this != NONE;
    }
}
