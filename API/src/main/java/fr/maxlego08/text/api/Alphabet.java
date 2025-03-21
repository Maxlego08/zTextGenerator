package fr.maxlego08.text.api;

import fr.maxlego08.text.api.records.FontInfo;

import java.io.File;
import java.util.List;

public interface Alphabet {

    String getName();

    File getFile();

    List<FontInfo> getFontInfos();

    FontTransformation getFontTransformation();

    int getTextLength(String content);

    int getLength(char c);

    String transformChar(char c, int height);
}
