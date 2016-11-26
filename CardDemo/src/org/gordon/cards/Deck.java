package org.gordon.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Abstraction to hold the complete set of playing cards and shuffle and deal
 * out cards.
 * 
 * Note the problem spec does *not* address whether concurrent access is
 * permissible. It is not clear that using a deck of cards from concurrent
 * threads makes sense, so we document here that the code is not thread
 * safe. Even if we made the methods synchronized, there would still be a race
 * condition from the time the user called isEmpty() to the invocation of
 * dealOneCard(), so more work would be required of the caller.
 * 
 * The other option of having dealOneCard() return null for an empty deck, to open
 * the possibility of using synchronized methods, is an invitation for trouble, as
 * the caller may end up with a hard-to-track-down NPE due to misuse. If we were
 * coding for JDK 8 onward, perhaps having dealOneCard() return an Optional could
 * arguably be considered.
 */
public class Deck {

    /**
     * Place to hold the cards. We use an ArrayList<Cards> rather than
     * an array of Cards (Card[]), which might be marginally more efficient,
     * as the list approach is more foolproof, plus allows for additional
     * functionality, such as sorting, if the need ever arises in the future.
     * Also, if someone else maintains this code, it is clearer to see what's
     * going on using the list operations versus a raw array.
     * 
     * We choose not to use java.util.Stack because that uses Vector, whose
     * methods are all synchronized.
     */
    private List<Card> cards;

    /**
     * We maintain an index to the list of cards, rather than deleting them.
     */
    private int cardsRemaining;

    /**
     * Needed for shuffle algorithm.
     */
    private Random generator = new Random();

    /**
     * Constructs a new unshuffled deck.
     */
    public Deck() {
        cards = new ArrayList<Card>(52); // Note 52 doesn't preclude a
                                         // larger list, say for Jokers.
        constructDeck();
        cardsRemaining = cards.size(); 
    }

    /**
     * Knuth O(n) shuffle. No return value, no possibility of exceptions.
     */
    public void shuffle() {
        // The algorithm stops at one, not zero because otherwise we'd always
        // swap 0 with 0.
        for (int i = cardsRemaining - 1; i > 0; --i) {
            // Random number in the range [0..i]
            int swapee = generator.nextInt(i + 1);
            Collections.swap(cards, i, swapee);
        }
    }

    /**
     * Reports whether the deck is empty.
     * @return {@code true} if the deck is empty {@code false} otherwise.
     */
    public boolean isEmpty() {
        return cardsRemaining == 0;
    }

    /**
     * Deal a card from the deck and throw an exception is no more cards are
     * left.
     * 
     * @return The next available card or {@code null} if the deck is empty.
     * @throws EmptyDeckException
     *             if the deck is empty.
     */
    public synchronized Card dealOneCard() throws EmptyDeckException {
        if (cardsRemaining == 0) {
            throw new EmptyDeckException("No cards remaining to deal!");
        }

        // Take the last card by decrementing the card count.
        return cards.get((cardsRemaining--) - 1);
    }
    
    /**
     * Return the number of cards left undealt in the deck.
     * @return The number of cards remaining in the deck.
     */
    public int getCardsRemaining() {
        return cardsRemaining;
    }
    
    /**
     * Because we don't ever actually delete the cards, we can reset the deck without
     * creating a new one.  If we recreated it, the card set would be identical anyway
     * (modulo the shuffle), so this saves recreating the same information, essentially.
     */
    public void reset() {
        cardsRemaining = cards.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        // A subclass of Deck is not equal to a Deck.
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        Deck d = (Deck) obj;
        if (cardsRemaining != d.cardsRemaining) {
            return false;
        }
        return cardsRemaining == 0
                || cards.subList(0, cardsRemaining).equals(d.cards.subList(0, cardsRemaining));
    }

    @Override
    public int hashCode() {
        // Doesn't have to be perfectly unique. Good enough for this demo!
        return super.hashCode();
    }

    // Iterate through all ranks/suits to build the deck.
    private void constructDeck() {
        for (Card.Rank r : Card.Rank.values()) {
            for (Card.Suit s : Card.Suit.values()) {
                cards.add(new Card(r, s));
            }
        }
    }
}
