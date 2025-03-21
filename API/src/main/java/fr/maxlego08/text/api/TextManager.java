package fr.maxlego08.text.api;

import java.io.File;
import java.util.List;
import java.util.Optional;

public interface TextManager {

    List<Alphabet> getAlphabets();

    Optional<Alphabet> getAlphabet(String name);

    void loadAlphabets();

    void loadAlphabet(File file);

    String getOffset();

    String getOffset(int pixels);

    String getNegativeOffset(int pixels);

}
