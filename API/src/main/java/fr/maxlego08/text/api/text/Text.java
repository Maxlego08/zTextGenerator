package fr.maxlego08.text.api.text;

import org.bukkit.entity.Player;

import java.util.List;

public interface Text {

    /**
     * Get the name of this text.
     *
     * @return the name of this text.
     */
    String getName();

    /**
     * Get the language code of this text.
     *
     * @return the language of this text, expressed using lowercase language and country codes (for example {@code en_us}).
     */
    String getLanguage();

    /**
     * Get the title of this text.
     *
     * @return the title of this text.
     */
    String getTitle();

    /**
     * Get the length of this text.
     *
     * @return the length of this text.
     */
    int getLength();

    /**
     * Get the lines of this text.
     *
     * @return the lines of this text.
     */
    List<TextLine> getLines();

    /**
     * Get the result of this text.
     *
     * <p>This method return the result of the text after it has been built.</p>
     *
     * @return the result of this text.
     */
    String getResult();

    /**
     * Check if this text has a result.
     *
     * <p>This method check if the result of this text has been created.</p>
     *
     * @return true if the result of this text has been created, false otherwise.
     */
    boolean hasResult();

    /**
     * Gets the result of this text for the given player.
     *
     * <p>This method returns the result of the text after it has been built. The result is built using the player's name.</p>
     *
     * @param player the player to get the result for
     * @return the result of this text for the given player
     */
    String getResult(Player player);

    /**
     * Creates the cached result of this text if the text does not have a placeholder.
     * This method builds the cached result of the text using its lines and elements. The result is built using the name of the player.
     *
     * @see #getResult(Player)
     */
    void createCacheResult();
}


