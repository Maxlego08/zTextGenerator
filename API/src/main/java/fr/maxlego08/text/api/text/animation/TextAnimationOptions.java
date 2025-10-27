package fr.maxlego08.text.api.text.animation;

/**
 * Options used to control how a text is displayed to a player.
 *
 * @param type              the animation type to use
 * @param initialDelayTicks the delay in ticks before the first frame is displayed
 * @param stepDelayTicks    the delay in ticks between each animation frame
 */
public record TextAnimationOptions(TextAnimationType type, long initialDelayTicks, long stepDelayTicks) {

    /**
     * Creates new animation options with the given parameters.
     *
     * @param type              the animation type to use
     * @param initialDelayTicks the delay in ticks before the first frame is displayed
     * @param stepDelayTicks    the delay in ticks between each animation frame
     */
    public TextAnimationOptions {
        type = type == null ? TextAnimationType.NONE : type;

        if (initialDelayTicks < 0) {
            throw new IllegalArgumentException("The initial delay must be greater than or equal to 0.");
        }

        if (type == TextAnimationType.NONE) {
            stepDelayTicks = 0;
        } else if (stepDelayTicks <= 0) {
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
     * @param delayTicks the delay between each letter in ticks
     * @return the created animation options
     */
    public static TextAnimationOptions letterByLetter(long delayTicks) {
        return letterByLetter(0L, delayTicks);
    }

    /**
     * Creates animation options revealing text letter by letter.
     *
     * @param initialDelayTicks the delay before the first letter is displayed
     * @param delayTicks        the delay between each letter in ticks
     * @return the created animation options
     */
    public static TextAnimationOptions letterByLetter(long initialDelayTicks, long delayTicks) {
        return new TextAnimationOptions(TextAnimationType.LETTER_BY_LETTER, initialDelayTicks, delayTicks);
    }

    /**
     * Creates animation options revealing text word by word.
     *
     * @param delayTicks the delay between each word in ticks
     * @return the created animation options
     */
    public static TextAnimationOptions wordByWord(long delayTicks) {
        return wordByWord(0L, delayTicks);
    }

    /**
     * Creates animation options revealing text word by word.
     *
     * @param initialDelayTicks the delay before the first word is displayed
     * @param delayTicks        the delay between each word in ticks
     * @return the created animation options
     */
    public static TextAnimationOptions wordByWord(long initialDelayTicks, long delayTicks) {
        return new TextAnimationOptions(TextAnimationType.WORD_BY_WORD, initialDelayTicks, delayTicks);
    }
}
