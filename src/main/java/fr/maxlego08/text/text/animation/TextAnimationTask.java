package fr.maxlego08.text.text.animation;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.text.animation.TextAnimationType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the display of a text to a player using different animation strategies.
 */
public class TextAnimationTask extends BukkitRunnable {

    private final TextPlugin plugin;
    private final Player player;
    private final String originalText;
    private final TextAnimationOptions options;
    private final ColorHelper colorHelper;
    private final List<String> frames;
    private int index = 0;

    public TextAnimationTask(TextPlugin plugin, Player player, String originalText, TextAnimationOptions options) {
        this.plugin = plugin;
        this.player = player;
        String sanitized = originalText == null ? "" : originalText;
        sanitized = plugin.getTextManager().stripOffsets(sanitized);
        this.originalText = sanitized;
        this.options = options;
        this.colorHelper = plugin.getColorHelper();
        this.frames = buildFrames(this.originalText, options.type());
    }

    /**
     * Starts the animation. If the animation type is {@link TextAnimationType#NONE} or the
     * text does not require animation, the text is sent immediately.
     */
    public void start() {
        if (this.frames.isEmpty()) {
            return;
        }

        if (this.options.type() == TextAnimationType.NONE || this.frames.size() <= 1 || this.options.stepDelayTicks() <= 0) {
            displayFrame(this.frames.get(this.frames.size() - 1));
            return;
        }

        this.runTaskTimer(this.plugin, this.options.initialDelayTicks(), this.options.stepDelayTicks());
    }

    @Override
    public void run() {
        if (!this.player.isOnline()) {
            this.cancel();
            return;
        }

        if (this.index >= this.frames.size()) {
            this.cancel();
            return;
        }

        displayFrame(this.frames.get(this.index));
        this.index++;

        if (this.index >= this.frames.size()) {
            this.cancel();
        }
    }

    private void displayFrame(String frame) {
        if (frame == null || frame.isEmpty() || !this.player.isOnline()) {
            return;
        }
        this.player.openInventory(this.colorHelper.createTextInventory(this.player, frame));
    }

    private List<String> buildFrames(String text, TextAnimationType type) {
        return switch (type) {
            case NONE -> List.of(text);
            case LETTER_BY_LETTER -> buildAnimatedFrames(text, false);
            case WORD_BY_WORD -> buildAnimatedFrames(text, true);
        };
    }

    private List<String> buildAnimatedFrames(String text, boolean wordMode) {
        List<String> frames = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return frames;
        }

        StringBuilder raw = new StringBuilder();
        List<String> openTags = new ArrayList<>();
        StringBuilder currentWord = new StringBuilder();

        for (int i = 0; i < text.length(); ) {
            char c = text.charAt(i);
            if (c == '<') {
                int tagEnd = findTagEnd(text, i);
                if (tagEnd == -1) {
                    raw.append(c);
                    if (!wordMode) {
                        addFrame(frames, buildFrame(raw, openTags));
                    } else {
                        currentWord.append(c);
                    }
                    i++;
                    continue;
                }

                String tag = text.substring(i, tagEnd + 1);
                raw.append(tag);
                processTag(tag, text, tagEnd, openTags);
                i = tagEnd + 1;
                continue;
            }

            raw.append(c);
            if (wordMode) {
                currentWord.append(c);
                if (Character.isWhitespace(c)) {
                    addFrame(frames, buildFrame(raw, openTags));
                    currentWord.setLength(0);
                }
            } else {
                addFrame(frames, buildFrame(raw, openTags));
            }
            i++;
        }

        if (wordMode && currentWord.length() > 0) {
            addFrame(frames, buildFrame(raw, openTags));
        }

        if (frames.isEmpty()) {
            frames.add(text);
        }

        return frames;
    }

    private int findTagEnd(String text, int start) {
        return text.indexOf('>', start);
    }

    private void processTag(String tag, String fullText, int tagEnd, List<String> openTags) {
        if (tag.length() < 3) {
            return;
        }

        if (tag.charAt(1) == '/') {
            String name = extractTagName(tag.substring(2, tag.length() - 1));
            if (!name.isEmpty()) {
                removeOpenTag(openTags, name);
            }
            return;
        }

        if (tag.endsWith("/>")) {
            return;
        }

        String inner = tag.substring(1, tag.length() - 1);
        String name = extractTagName(inner);
        if (name.isEmpty()) {
            return;
        }

        if (!hasClosingTag(fullText, tagEnd + 1, name)) {
            return;
        }

        openTags.add(name);
    }

    private boolean hasClosingTag(String text, int fromIndex, String tagName) {
        return text.indexOf("</" + tagName + ">", fromIndex) != -1;
    }

    private String extractTagName(String tagContent) {
        if (tagContent == null || tagContent.isEmpty()) {
            return "";
        }
        int end = tagContent.length();
        int colonIndex = tagContent.indexOf(':');
        int spaceIndex = tagContent.indexOf(' ');
        if (colonIndex != -1 && colonIndex < end) {
            end = colonIndex;
        }
        if (spaceIndex != -1 && spaceIndex < end) {
            end = spaceIndex;
        }
        if (end <= 0) {
            return "";
        }
        return tagContent.substring(0, end);
    }

    private void removeOpenTag(List<String> openTags, String name) {
        for (int i = openTags.size() - 1; i >= 0; i--) {
            if (openTags.get(i).equalsIgnoreCase(name)) {
                openTags.remove(i);
                break;
            }
        }
    }

    private String buildFrame(CharSequence raw, List<String> openTags) {
        if (raw.length() == 0) {
            return "";
        }

        if (openTags.isEmpty()) {
            return raw.toString();
        }

        StringBuilder builder = new StringBuilder(raw);
        for (int i = openTags.size() - 1; i >= 0; i--) {
            builder.append("</").append(openTags.get(i)).append(">");
        }
        return builder.toString();
    }

    private void addFrame(List<String> frames, String frame) {
        if (frame == null || frame.isEmpty()) {
            return;
        }
        if (frames.isEmpty() || !frames.get(frames.size() - 1).equals(frame)) {
            frames.add(frame);
        }
    }
}
