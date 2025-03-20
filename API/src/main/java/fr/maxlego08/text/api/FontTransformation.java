package fr.maxlego08.text.api;

import fr.maxlego08.text.api.records.SpecialFontTransformation;

import java.util.List;
import java.util.Optional;

public interface FontTransformation {

    List<SpecialFontTransformation> getFontTransformations();

    String getUpperCase();

    String getLowerCase();

    String transformChar(char c, int height);

    Optional<SpecialFontTransformation> getFontTransformation(char c);

}
