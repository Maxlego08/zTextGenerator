package fr.maxlego08.text.api.placeholders;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class AutoPlaceholder {

    private final String startWith;
    private final BiFunction<Player, String, String> biConsumer;
    private final Function<Player, String> consumer;
    private final String description;
    private final List<String> args;

    public AutoPlaceholder(String startWith, BiFunction<Player, String, String> biConsumer, String description, List<String> args) {
        super();
        this.startWith = startWith;
        this.biConsumer = biConsumer;
        this.description = description;
        this.args = args;
        this.consumer = null;
    }

    public AutoPlaceholder(String startWith, Function<Player, String> consumer, String description, List<String> args) {
        this.startWith = startWith;
        this.description = description;
        this.args = args;
        this.biConsumer = null;
        this.consumer = consumer;
    }

    /**
     * Retrieves the prefix string that this placeholder starts with.
     * <p>
     * This method returns the value of the startWith field,
     * which is used to determine if a string matches the pattern
     * of this placeholder.
     *
     * @return the startWith string
     */
    public String getStartWith() {
        return startWith;
    }

    /**
     * Gets the bi-consumer function to be applied to the player and arguments.
     * <p>
     * This bi-consumer function is used when the placeholder is used with arguments.
     * It takes a Player and a String as input and returns a String as output.
     *
     * @return the bi-consumer function, or null if this placeholder doesn't use a bi-consumer
     */
    public BiFunction<Player, String, String> getBiConsumer() {
        return biConsumer;
    }

    /**
     * Gets the consumer function to be applied to the player.
     * <p>
     * This consumer function is used when the placeholder is used without any arguments.
     * It takes a Player as input and returns a String as output.
     *
     * @return the consumer function
     */
    public Function<Player, String> getConsumer() {
        return this.consumer;
    }

    /**
     * Accepts a Player and a value to process with the appropriate consumer.
     * <p>
     * If the consumer is not null, it applies the consumer function to the player.
     * If the biConsumer is not null, it applies the biConsumer function to the player and value.
     *
     * @param player the Player object to be processed
     * @param value  the String value to be processed with the biConsumer
     * @return the result of the consumer or biConsumer function, or an error message if neither is defined
     */
    public String accept(Player player, String value) {
        if (this.consumer != null) return this.consumer.apply(player);
        if (this.biConsumer != null) return this.biConsumer.apply(player, value);
        return "Error with consumer !";
    }

    /**
     * Determines if the given string starts with the value of {@link #startWith}.
     * <p>
     * If the consumer is not null, it will compare the string to the startWith value using a case-insensitive comparison.
     * If the biConsumer is not null, it will compare the string to the startWith value using a case-sensitive startsWith comparison.
     *
     * @param string the string to check
     * @return true if the string starts with the startWith value, false otherwise
     */
    public boolean startsWith(String string) {
        return this.consumer != null ? this.startWith.equalsIgnoreCase(string) : string.startsWith(this.startWith);
    }

    /**
     * Gets the description of the {@link AutoPlaceholder}. If the description is not explicitly set, it will default to an empty string.
     *
     * @return the description of the {@link AutoPlaceholder}
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retrieves the list of arguments associated with this AutoPlaceholder.
     *
     * @return a list of strings representing the arguments
     */
    public List<String> getArgs() {
        return args;
    }
}
