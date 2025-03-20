package fr.maxlego08.text.api.records;

public record SpecialFontTransformation(char character, String replacement) {

    public String transform(int height) {
        return this.replacement.replace("%height%", String.valueOf(height));
    }
}
