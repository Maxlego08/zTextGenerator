package fr.maxlego08.text.zcore.utils.documentations;

import fr.maxlego08.text.api.placeholders.AutoPlaceholder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderMarkdownGenerator {

    /**
     * Generates a Markdown file containing a table of placeholders and their descriptions.
     *
     * <p>This method takes a list of {@link AutoPlaceholder} objects and generates a Markdown
     * table with two columns: "Placeholder" and "Description". Each row in the table represents
     * a placeholder, including its formatted name and description. The placeholders are sorted
     * alphabetically by their start string before being written to the file.
     *
     * @param placeholders a list of {@link AutoPlaceholder} objects to be included in the Markdown table
     * @param filePath     the {@link Path} where the generated Markdown file will be written
     * @throws IOException if an I/O error occurs writing to or creating the file
     */
    public void generateMarkdownFile(List<AutoPlaceholder> placeholders, Path filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        // Markdown table header
        sb.append("| Placeholder | Description |\n");
        sb.append("|--------------|-------------|\n");

        placeholders.sort(Comparator.comparing(AutoPlaceholder::getStartWith));
        for (AutoPlaceholder placeholder : placeholders) {
            // Format placeholder with arguments
            String placeholderText = "%ztextgenerator_" + placeholder.getStartWith();
            if (!placeholder.getArgs().isEmpty()) {
                String args = placeholder.getArgs().stream().map(argument -> "<" + argument + ">").collect(Collectors.joining("_"));
                placeholderText += args;
            }
            placeholderText += "%";

            // Escape Markdown special characters in descriptions
            String desc = placeholder.getDescription().replace("|", "\\|");

            // Add row to the Markdown table
            sb.append(String.format("| `%s` | %s |\n", placeholderText, desc));
        }

        // Write the StringBuilder content to the file
        Files.writeString(filePath, sb.toString());
    }
}
