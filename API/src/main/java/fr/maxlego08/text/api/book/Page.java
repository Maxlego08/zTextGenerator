package fr.maxlego08.text.api.book;

import fr.maxlego08.text.api.text.TextLine;

import java.util.List;

public record Page(PageType pageType, List<TextLine> textLines) {
}
