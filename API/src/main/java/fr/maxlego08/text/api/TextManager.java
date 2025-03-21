package fr.maxlego08.text.api;

import fr.maxlego08.text.api.text.Text;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TextManager {

    List<Alphabet> getAlphabets();

    Optional<Alphabet> getAlphabet(String name);

    void loadTexts();

    void loadTexts(File file);

    void loadText(Map<?, ?> map);

    void loadAlphabets();

    void loadAlphabet(File file);

    String getOffset();

    String getOffset(int pixels);

    String getNegativeOffset(int pixels);

    Alphabet getInventoryTitleAlphabet();

    List<Text> getTexts();

    Optional<Text> getText(String name);

}
