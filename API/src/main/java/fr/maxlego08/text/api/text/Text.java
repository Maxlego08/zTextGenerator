package fr.maxlego08.text.api.text;

import fr.maxlego08.text.api.Alphabet;
import fr.maxlego08.text.api.utils.Alignment;
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
     * Creates the result of this text.
     *
     * <p>This method builds the result of the text using its lines and elements.</p>
     *
     * @see #getResult()
     */
    void createResult();

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
}


