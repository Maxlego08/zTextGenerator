package fr.maxlego08.text.api.book;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.utils.Alignment;

public record PageContent(Alignment alignment, Alphabet alphabet, String content) {
}
