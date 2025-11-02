package fr.maxlego08.text.api.fonts;

public record SpecialFontTransformation(char character, String replacement) {

    public String transform(int height) {
        return this.replacement.replace("%height%", String.valueOf(height));
    }
}
