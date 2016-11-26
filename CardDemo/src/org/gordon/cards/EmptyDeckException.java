package org.gordon.cards;

/**
 * Used to indicate a call was made to deal a card, but the deck was empty.
 */
public class EmptyDeckException extends Exception {

    /**
     * An exception indicating the deck was empty, with an error message.
     * @param message the error message
     */
    public EmptyDeckException(String message) {
        super(message);
    }

    /**
     * An exception indicating the deck was empty, with an error message and throwable cause
     * @param message the error message
     * @param t the cause
     */
    public EmptyDeckException(String message, Throwable t) {
        super(message, t);
    }

    static final long serialVersionUID = -687991492884005033L;
}