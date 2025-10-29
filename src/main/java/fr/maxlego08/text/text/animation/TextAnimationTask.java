package fr.maxlego08.text.text.animation;

import fr.maxlego08.text.TextPlugin;
import fr.maxlego08.text.ZTextManager;
import fr.maxlego08.text.api.color.ColorHelper;
import fr.maxlego08.text.api.text.Text;
import fr.maxlego08.text.api.text.animation.TextAnimationOptions;
import fr.maxlego08.text.api.text.animation.TextAnimationType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Handles the display of a text to a player using different animation strategies.
 */
public class TextAnimationTask extends BukkitRunnable {

    private final TextPlugin plugin;
    private final Player player;
    private final UUID playerId;
    private final String originalText;
    private final TextAnimationOptions options;
    private final ColorHelper colorHelper;
    private final List<String> frames;
    private final String finalFrame;
    private final boolean animated;
    private final ZTextManager textManager;
    private final String inventoryName;
    private final int inventorySize;
    private boolean stopped = false;
    private int index = 0;

    public TextAnimationTask(TextPlugin plugin, ZTextManager textManager, Player player, Text text, String originalText, TextAnimationOptions options) {
        this.plugin = plugin;
        this.textManager = textManager;
        this.player = player;
        this.playerId = player.getUniqueId();
        this.originalText = originalText == null ? "" : originalText;
        this.options = options == null ? TextAnimationOptions.none() : options;
        this.colorHelper = plugin.getColorHelper();
        TextAnimationType animationType = this.options.type();
        this.frames = buildFrames(this.originalText, animationType);
        this.finalFrame = this.originalText;
        this.animated = animationType != TextAnimationType.NONE && this.frames.size() > 1 && this.options.stepDelayMillis() > 0;
        String prefix = text == null ? "" : text.getInventoryName(player);
        this.inventoryName = prefix == null ? "" : prefix;
        this.inventorySize = text == null ? 54 : text.getInventorySize();
    }

    /**
     * Starts the animation. If the animation type is {@link TextAnimationType#NONE} or the
     * text does not require animation, the text is sent immediately.
     */
    public void start() {

        if (!this.animated || this.frames.isEmpty()) {
            displayFrame(this.finalFrame);
            this.textManager.onAnimationStopped(this.playerId, this);
            return;
        }

        long initialDelayTicks = this.options.initialDelayTicks();
        long stepDelayTicks = this.options.stepDelayTicks();
        if (stepDelayTicks <= 0) {
            displayFrame(this.finalFrame);
            this.textManager.onAnimationStopped(this.playerId, this);
            return;
        }

        this.runTaskTimer(this.plugin, initialDelayTicks, stepDelayTicks);
    }

    @Override
    public void run() {
        if (!this.player.isOnline()) {
            stop();
            return;
        }

        if (this.index >= this.frames.size()) {
            stop();
            return;
        }

        displayFrame(this.frames.get(this.index));
        this.index++;

        if (this.index >= this.frames.size()) {
            stop();
        }
    }

    private void displayFrame(String frame) {
        if (!this.player.isOnline()) {
            return;
        }
        String text = (this.inventoryName == null ? "" : this.inventoryName) + (frame == null ? "" : frame);
        if (text.isEmpty()) {
            return;
        }
        this.player.setMetadata("cant-close-inventory", new FixedMetadataValue(this.plugin, true));
        this.player.openInventory(this.colorHelper.createTextInventory(this.player, this.inventorySize, text));
        this.player.removeMetadata("cant-close-inventory", this.plugin);
    }

    public boolean isAnimated() {
        return this.animated;
    }

    public void cancelAnimation() {
        stop();
    }

    public void stopAndShowFinal() {
        stop();
        displayFrame(this.finalFrame);
    }

    private void stop() {
        if (this.stopped) {
            return;
        }
        this.stopped = true;
        if (this.getTaskId() != -1) {
            super.cancel();
        }
        this.textManager.onAnimationStopped(this.playerId, this);
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        stop();
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

            if (c == ':') {
                int tokenEnd = findCustomTokenEnd(text, i);
                if (tokenEnd != -1) {
                    String token = text.substring(i, tokenEnd + 1);
                    raw.append(token);
                    if (wordMode) {
                        currentWord.append(token);
                    }
                    i = tokenEnd + 1;
                    continue;
                }
            }

            if (Character.isWhitespace(c)) {
                int whitespaceEnd = i + 1;
                while (whitespaceEnd < text.length() && Character.isWhitespace(text.charAt(whitespaceEnd))) {
                    whitespaceEnd++;
                }

                String whitespaces = text.substring(i, whitespaceEnd);
                raw.append(whitespaces);
                if (wordMode) {
                    currentWord.append(whitespaces);
                    addFrame(frames, buildFrame(raw, openTags));
                    currentWord.setLength(0);
                } else {
                    addFrame(frames, buildFrame(raw, openTags));
                }

                i = whitespaceEnd;
                continue;
            }

            raw.append(c);
            if (wordMode) {
                currentWord.append(c);
            } else {
                addFrame(frames, buildFrame(raw, openTags));
            }
            i++;
        }

        if (wordMode && !currentWord.isEmpty()) {
            addFrame(frames, buildFrame(raw, openTags));
        }

        if (!frames.isEmpty()) {
            String completed = buildFrame(raw, openTags);
            if (!completed.isEmpty()) {
                frames.set(frames.size() - 1, completed);
            }
        }

        if (frames.isEmpty()) {
            frames.add(text);
        }

        return frames;
    }

    private int findTagEnd(String text, int start) {
        return text.indexOf('>', start);
    }

    private int findCustomTokenEnd(String text, int start) {
        int end = text.indexOf(':', start + 1);
        if (end == -1) {
            return -1;
        }
        for (int i = start + 1; i < end; i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                return -1;
            }
        }
        return end;
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
        if (end == 0) {
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
        if (raw.isEmpty()) {
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

        if (frames.isEmpty() || !frames.getLast().equals(frame)) {
            frames.add(frame);
        }
    }
}
