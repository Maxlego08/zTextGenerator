package fr.maxlego08.text.zcore.utils.documentations;

import fr.maxlego08.text.api.commands.VCommand;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandMarkdownGenerator {


    /**
     * Generates a Markdown file containing a table of commands and their details.
     *
     * <p>This method takes a list of {@link VCommand} objects and generates a Markdown
     * table with four columns: "Command", "Aliases", "Permission", and "Description".
     * Each row in the table represents a command, including its syntax, any aliases,
     * the permission required to run the command, and a brief description of the
     * command. The commands are sorted alphabetically by their syntax before being
     * written to the file.
     *
     * @param commands a list of {@link VCommand} objects to be included in the Markdown table
     * @param filePath the {@link Path} where the generated Markdown file will be written
     * @throws IOException if an I/O error occurs writing to or creating the file
     */
    public void generateMarkdownFile(List<VCommand> commands, Path filePath) throws IOException {

        List<VCommand> newCommands = new ArrayList<>();
        commands.stream().filter(e -> e.getParent() == null).sorted(Comparator.comparing(VCommand::getFirst)).forEach(command -> {
            newCommands.add(command);
            newCommands.addAll(commands.stream().filter(e -> e.getMainParent() == command).sorted(Comparator.comparing(VCommand::getFirst)).toList());
        });

        StringBuilder sb = new StringBuilder();
        // Markdown table header
        sb.append("| Command | Aliases | Permission | Description |\n");
        sb.append("|---------|---------|------------|-------------|\n");

        for (VCommand command : newCommands) {
            // Gather command data
            String cmd = command.getSyntax(); // Assuming getSyntax() gives the command
            List<String> aliasesList = new ArrayList<>(command.getSubCommands());
            if (!aliasesList.isEmpty()) {
                aliasesList.removeFirst();  // Remove the first element
            }
            String aliases = aliasesList.stream()
                    .map(alias -> "/" + alias)  // Add '/' before each alias
                    .collect(Collectors.joining(", "));
            String perm = command.getPermission(); // getPermission() for permissions
            String desc = command.getDescription(); // getDescription() for the description

            // Escape special Markdown characters in descriptions
            desc = desc == null ? "" : desc.replace("|", "\\|");
            perm = perm == null ? "" : perm;

            // Add row to the Markdown table
            sb.append(String.format("| `%s` | %s | %s | %s |\n", cmd, aliases, perm, desc));
        }

        // Write the StringBuilder content to the file
        Files.writeString(filePath, sb.toString());
    }
}
