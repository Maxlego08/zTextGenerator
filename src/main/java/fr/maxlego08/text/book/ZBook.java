package fr.maxlego08.text.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;

import java.util.List;
import java.util.Optional;

public class ZBook implements Book {

    private final String name;
    private final String inventoryName;
    private final List<BookPage> pages;
    private final int startOffset;
    private final int leftSize;
    private final int rightSize;
    private final int offsetBetween;
    private final Alphabet alphabet;

    public ZBook(String name, String inventoryName, List<BookPage> pages, int startOffset, int leftSize, int rightSize, int offsetBetween, Alphabet alphabet) {
        this.name = name;
        this.inventoryName = inventoryName;
        this.pages = pages;
        this.startOffset = startOffset;
        this.leftSize = leftSize;
        this.rightSize = rightSize;
        this.offsetBetween = offsetBetween;
        this.alphabet = alphabet;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getInventoryName() {
        return inventoryName;
    }

    @Override
    public List<BookPage> getPages() {
        return this.pages;
    }

    @Override
    public int getStartOffset() {
        return this.startOffset;
    }

    @Override
    public int getLeftSize() {
        return this.leftSize;
    }

    @Override
    public int getRightSize() {
        return this.rightSize;
    }

    @Override
    public int getOffsetBetween() {
        return this.offsetBetween;
    }

    @Override
    public Alphabet getAlphabet() {
        return alphabet;
    }

    @Override
    public Optional<BookPage> getPage(int page) {
        return this.pages.stream().filter(bookPage -> bookPage.page() == page).findFirst();
    }

    @Override
    public String toString() {
        return "ZBook{" +
                "name='" + name + '\'' +
                ", inventoryName='" + inventoryName + '\'' +
                ", pages=" + pages +
                ", startOffset=" + startOffset +
                ", leftSize=" + leftSize +
                ", rightSize=" + rightSize +
                ", offsetBetween=" + offsetBetween +
                ", alphabet=" + alphabet +
                '}';
    }
}
