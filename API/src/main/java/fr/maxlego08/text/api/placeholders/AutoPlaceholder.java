package fr.maxlego08.text.api.placeholders;

import org.bukkit.entity.Player;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AutoPlaceholder {

    private final String startWith;
    private final BiFunction<Player, String, String> biConsumer;
    private final Function<Player, String> consumer;

    public AutoPlaceholder(String startWith, BiFunction<Player, String, String> biConsumer) {
        super();
        this.startWith = startWith;
        this.biConsumer = biConsumer;
        this.consumer = null;
    }

    public AutoPlaceholder(String startWith, Function<Player, String> consumer) {
        this.startWith = startWith;
        this.biConsumer = null;
        this.consumer = consumer;
    }

    public String getStartWith() {
        return startWith;
    }

    public BiFunction<Player, String, String> getBiConsumer() {
        return biConsumer;
    }

    public Function<Player, String> getConsumer() {
        return this.consumer;
    }

    public String accept(Player player, String value) {
        if (this.consumer != null) return this.consumer.apply(player);
        if (this.biConsumer != null) return this.biConsumer.apply(player, value);
        return "Error with consumer !";
    }

    public boolean startsWith(String string) {
        return this.consumer != null ? this.startWith.equalsIgnoreCase(string) : string.startsWith(this.startWith);
    }
}
