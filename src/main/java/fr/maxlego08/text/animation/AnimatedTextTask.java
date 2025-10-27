package fr.maxlego08.text.animation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class AnimatedTextTask extends BukkitRunnable {

    private static final Pattern WORD_PATTERN = Pattern.compile("\\S+\\s*");

    private final CommandSender receiver;
    private final String text;
    private final AnimationMode mode;
    private final StringBuilder current = new StringBuilder();
    private final List<String> wordTokens;

    private int charIndex = 0;
    private int tokenIndex = 0;

    public AnimatedTextTask(CommandSender receiver, String text, AnimationMode mode) {
        this.receiver = receiver;
        this.text = text;
        this.mode = mode;
        this.wordTokens = mode == AnimationMode.WORD_BY_WORD ? splitTextIntoWordTokens(text) : List.of();
    }

    @Override
    public void run() {
        switch (mode) {
            case NONE -> {
                receiver.sendMessage(text);
                cancel();
            }
            case LETTER_BY_LETTER -> playLetterByLetter();
            case WORD_BY_WORD -> playWordByWord();
        }
    }

    private void playLetterByLetter() {
        if (charIndex >= text.length()) {
            cancel();
            return;
        }
        current.append(text.charAt(charIndex++));
        receiver.sendMessage(current.toString());
        if (charIndex >= text.length()) {
            cancel();
        }
    }

    private void playWordByWord() {
        if (tokenIndex >= wordTokens.size()) {
            cancel();
            return;
        }
        current.append(wordTokens.get(tokenIndex++));
        receiver.sendMessage(current.toString());
        if (tokenIndex >= wordTokens.size()) {
            cancel();
        }
    }

    private static List<String> splitTextIntoWordTokens(String input) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = WORD_PATTERN.matcher(input);
        int lastIndex = 0;
        while (matcher.find()) {
            tokens.add(input.substring(lastIndex, matcher.end()));
            lastIndex = matcher.end();
        }
        if (lastIndex < input.length()) {
            tokens.add(input.substring(lastIndex));
        }
        if (tokens.isEmpty()) {
            tokens.add(input);
        }
        return tokens;
    }
}
