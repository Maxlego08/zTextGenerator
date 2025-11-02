package fr.maxlego08.text.api.messages;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Message {

    PREFIX("&8(#2ce682zTextGenerator&8) "),
    COMMAND_SYNTAX_ERROR("&cYou must execute the command like this&7: &a%syntax%"),
    COMMAND_NO_PERMISSION("&cYou do not have permission to run this command."),
    COMMAND_NO_CONSOLE("&cOnly one player can execute this command."),
    COMMAND_NO_ARG("&cImpossible to find the command with its arguments."),
    COMMAND_SYNTAX_HELP("&f%syntax% &7» &7%description%"),

    RELOAD("&aYou have just reloaded the configuration files."),
    DESCRIPTION_RELOAD("Reload configuration files"),
    DESCRIPTION_BOOK("Open a book"),
    DESCRIPTION_OPEN("Open a text"),
    DESCRIPTION_TEST_ALPHABET("Manage alphabet testing"),

    BOOK_NOT_FOUND("&cImpossible to find the book &f%book%&c."),
    BOOK_PAGE_NOT_FOUND("&cImpossible to find the book page &f%page%&c."),
    TEXT_NOT_FOUND("&cImpossible to find the text &f%text%&c."),
    TEXT_PLAYER_NOT_FOUND("&cImpossible to find the player &f%player%&c."),
    TEXT_ANIMATION_TYPE_UNKNOWN("&cUnknown animation type &f%type%&c."),
    TEXT_ANIMATION_SPEED_INVALID("&cThe animation speed must be greater than 0."),
    ALPHABET_NOT_FOUND("&cImpossible to find the alphabet &f%alphabet%&c."),
    ALPHABET_VALIDATION_STARTED("&aAlphabet validation started for &f%alphabet% &a(every &f%delay% &asecond(s))."),
    ALPHABET_VALIDATION_ALREADY_RUNNING("&cYou already have an active alphabet validation."),
    ALPHABET_VALIDATION_DELAY_INVALID("&cThe delay between two letters must be at least 1 second."),
    ALPHABET_VALIDATION_EMPTY("&cThe selected alphabet does not contain any letters to validate."),
    ALPHABET_VALIDATION_CANCELLED("&aYou stopped the alphabet validation for &f%player%&a."),
    ALPHABET_VALIDATION_CANCELLED_TARGET("&cYour alphabet validation has been cancelled."),
    ALPHABET_VALIDATION_NOT_RUNNING("&cNo alphabet validation is currently running for &f%player%&c."),
    ALPHABET_VALIDATION_COMPLETED("&aAlphabet validation finished for &f%alphabet%&a.");

    private List<String> messages;
    private String message;
    private MessageType type = MessageType.TCHAT;

    /**
     * Constructs a new Message with the specified message string.
     *
     * @param message the message string.
     */
    Message(String message) {
        this.message = message;
    }

    /**
     * Constructs a new Message with multiple message strings.
     *
     * @param message the array of message strings.
     */
    Message(String... message) {
        this.messages = Arrays.asList(message);
    }

    /**
     * Constructs a new Message with a specific type and multiple message strings.
     *
     * @param type    the type of the message.
     * @param message the array of message strings.
     */
    Message(MessageType type, String... message) {
        this.messages = Arrays.asList(message);
        this.type = type;
    }

    /**
     * Constructs a new Message with a specific type and a single message string.
     *
     * @param type    the type of the message.
     * @param message the message string.
     */
    Message(MessageType type, String message) {
        this.message = message;
        this.type = type;
    }

    /**
     * Gets the message string.
     *
     * @return the message string.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Converts the message to a string.
     *
     * @return the message string.
     */
    public String toMsg() {
        return message;
    }

    /**
     * Gets the message string.
     *
     * @return the message string.
     */
    public String msg() {
        return message;
    }

    /**
     * Gets the list of messages.
     *
     * @return the list of messages.
     */
    public List<String> getMessages() {
        return messages == null ? Collections.singletonList(message) : messages;
    }

    /**
     * Sets the list of messages.
     *
     * @param messages the list of messages.
     */
    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    /**
     * Checks if the message contains multiple parts.
     *
     * @return true if the message contains multiple parts, false otherwise.
     */
    public boolean isMessage() {
        return messages != null && messages.size() > 1;
    }

    /**
     * Sets the message string.
     *
     * @param message the message string.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Replaces a substring in the message with another string.
     *
     * @param a the substring to replace.
     * @param b the replacement string.
     * @return the modified message string.
     */
    public String replace(String a, String b) {
        return message.replace(a, b);
    }

    /**
     * Gets the type of the message.
     *
     * @return the type of the message.
     */
    public MessageType getType() {
        return type;
    }

    /**
     * Sets the type of the message.
     *
     * @param type the type of the message.
     */
    public void setType(MessageType type) {
        this.type = type;
    }

}
