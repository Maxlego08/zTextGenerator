package fr.maxlego08.text;

import fr.maxlego08.text.api.FontTransformation;
import fr.maxlego08.text.api.fonts.SpecialFontTransformation;

import java.util.List;
import java.util.Optional;

public class ZFontTransformation implements FontTransformation {

    private final String upperCase;
    private final String lowerCase;
    private final List<SpecialFontTransformation> fontTransformations;

    public ZFontTransformation(String upperCase, String lowerCase, List<SpecialFontTransformation> fontTransformations) {
        this.upperCase = upperCase;
        this.lowerCase = lowerCase;
        this.fontTransformations = fontTransformations;
    }

    @Override
    public String getUpperCase() {
        return upperCase;
    }

    @Override
    public String getLowerCase() {
        return lowerCase;
    }

    @Override
    public String transformChar(char c, int height) {

        var optional = this.getFontTransformation(c);
        if (optional.isPresent()) {
            var fontTransformation = optional.get();
            return fontTransformation.transform(height);
        }

        String charStr = String.valueOf(c);
        String heightStr = String.valueOf(height);

        if (Character.isUpperCase(c)) {
            return this.upperCase.replace("%height%", heightStr).replace("%char%", charStr);
        } else if (Character.isLowerCase(c)) {
            return this.lowerCase.replace("%height%", heightStr).replace("%char%", charStr);
        }

        return null;
    }

    @Override
    public List<SpecialFontTransformation> getFontTransformations() {
        return fontTransformations;
    }

    @Override
    public Optional<SpecialFontTransformation> getFontTransformation(char c) {
        return this.fontTransformations.stream().filter(e -> e.character() == c).findFirst();
    }
}
