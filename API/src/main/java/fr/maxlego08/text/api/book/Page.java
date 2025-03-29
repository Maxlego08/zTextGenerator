package fr.maxlego08.text.api.book;

import java.util.List;

public record Page(PageType pageType, List<PageContent> pageContents) {
}
