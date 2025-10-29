package fr.maxlego08.text.api.text.animation;

/**
 * Options used to control how a text is displayed to a player.
 *
 * @param type               the animation type to use
 * @param initialDelayMillis the delay in milliseconds before the first frame is displayed
 * @param stepDelayMillis    the delay in milliseconds between each animation frame
 */
public record TextAnimationOptions(TextAnimationType type, long initialDelayMillis, long stepDelayMillis) {

    /**
     * Creates new animation options with the given parameters.
     *
     * @param type               the animation type to use
     * @param initialDelayMillis the delay in milliseconds before the first frame is displayed
     * @param stepDelayMillis    the delay in milliseconds between each animation frame
     */
    public TextAnimationOptions {
        type = type == null ? TextAnimationType.NONE : type;

        if (initialDelayMillis < 0) {
            throw new IllegalArgumentException("The initial delay must be greater than or equal to 0.");
        }

        if (type == TextAnimationType.NONE) {
            stepDelayMillis = 0;
        } else if (stepDelayMillis <= 0) {
            throw new IllegalArgumentException("The animation delay must be greater than 0 for animated text.");
        }
    }

    /**
     * Creates animation options without any animation.
     *
     * @return the created animation options
     */
    public static TextAnimationOptions none() {
        return new TextAnimationOptions(TextAnimationType.NONE, 0L, 0L);
    }

    /**
     * Creates animation options revealing text letter by letter.
     *
     * @param delayMillis the delay between each letter in milliseconds
     * @return the created animation options
     */
    public static TextAnimationOptions letterByLetter(long delayMillis) {
        return letterByLetter(0L, delayMillis);
    }

    /**
     * Creates animation options revealing text letter by letter.
     *
     * @param initialDelayMillis the delay before the first letter is displayed
     * @param delayMillis        the delay between each letter in milliseconds
     * @return the created animation options
     */
    public static TextAnimationOptions letterByLetter(long initialDelayMillis, long delayMillis) {
        return new TextAnimationOptions(TextAnimationType.LETTER_BY_LETTER, initialDelayMillis, delayMillis);
    }

    /**
     * Creates animation options revealing text word by word.
     *
     * @param delayMillis the delay between each word in milliseconds
     * @return the created animation options
     */
    public static TextAnimationOptions wordByWord(long delayMillis) {
        return wordByWord(0L, delayMillis);
    }

    /**
     * Creates animation options revealing text word by word.
     *
     * @param initialDelayMillis the delay before the first word is displayed
     * @param delayMillis        the delay between each word in milliseconds
     * @return the created animation options
     */
    public static TextAnimationOptions wordByWord(long initialDelayMillis, long delayMillis) {
        return new TextAnimationOptions(TextAnimationType.WORD_BY_WORD, initialDelayMillis, delayMillis);
    }

    /**
     * Gets the initial delay converted into ticks.
     *
     * @return the initial delay in ticks
     */
    public long initialDelayTicks() {
        return toTicks(initialDelayMillis);
    }

    /**
     * Gets the step delay converted into ticks.
     *
     * @return the step delay in ticks
     */
    public long stepDelayTicks() {
        return toTicks(stepDelayMillis);
    }

    private long toTicks(long millis) {
        if (millis <= 0) {
            return 0L;
        }
        long ticks = Math.round(millis / 50.0D);
        return Math.max(1L, ticks);
    }
}
