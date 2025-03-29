package fr.maxlego08.text.api.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.TextManager;
import fr.maxlego08.text.api.color.ColorHelper;

import java.util.List;
import java.util.Optional;

public interface Book {

    String getName();

    String getInventoryName();

    List<BookPage> getPages();

    int getStartOffset();

    int getLeftSize();

    int getRightSize();

    int getOffsetBetween();

    Alphabet getAlphabet();

    Optional<BookPage> getPage(int page);

    String toBookString(TextManager textManager, ColorHelper colorHelper, BookPage page);

}
