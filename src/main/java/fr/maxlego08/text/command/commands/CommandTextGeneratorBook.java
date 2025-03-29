package fr.maxlego08.text.command.commands;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.book.Book;
import fr.maxlego08.text.api.book.BookPage;
import fr.maxlego08.text.api.messages.Message;
import fr.maxlego08.text.api.utils.Permission;
import fr.maxlego08.text.command.VCommand;
import fr.maxlego08.text.zcore.utils.commands.CommandType;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.stream.Collectors;

public class CommandTextGeneratorBook extends VCommand {

    public CommandTextGeneratorBook(TextPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.ZTEXTGENERATOR_BOOK);
        this.addSubCommand("book");
        this.setDescription(Message.DESCRIPTION_BOOK);
        this.addRequireArg("player");
        this.addRequireArg("book", (a, b) -> plugin.getTextManager().getBooks().stream().map(Book::getName).collect(Collectors.toList()));
        this.addRequireArg("page", (sender, args) -> {
            String bookName = args[2];
            var optional = plugin.getTextManager().getBook(bookName);
            if (optional.isEmpty()) return List.of();
            var book = optional.get();
            return book.getPages().stream().map(BookPage::page).map(String::valueOf).collect(Collectors.toList());
        });
    }

    @Override
    protected CommandType perform(TextPlugin plugin) {

        Player player = this.argAsPlayer(0);
        String bookName = this.argAsString(1);
        int page = this.argAsInteger(2);
        plugin.getTextManager().openBook(sender, player, bookName, page);

        return CommandType.SUCCESS;
    }
}
