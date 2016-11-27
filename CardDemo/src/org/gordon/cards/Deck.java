package org.gordon.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Abstraction to hold the complete set of playing cards and shuffle and deal
 * out cards.
 * 
 * Please see the class design notes here (just below) - I've included them in case
 * we don't have a chance to discuss my design decisions.
 * 
 * The problem spec says to "assume the principle of least surprise".  However,
 * the spec does *not* address whether concurrent access of the deck is permissible.
 * After playing around with various implementations, I came to the conclusion that
 * the most prudent course of action is to leave everything not thread-safe and
 * document it is as such.  The caller should add concurrency protection as needed
 * in their code, and making our methods synchronized will not really help much anyway.
 * 
 * The reason for this decision is that after exploring various design alternatives,
 * I concluded that there is a legitimate use case for the user checking whether the
 * deck is empty without drawing a card.  And given that this involves two separate methods,
 * there will always be a race condition between the time the user checks whether the deck
 * is empty and then retrieves the deck.
 * 
 * Now we could have eliminated the empty deck check call by having drawOneCard() return
 * null for an empty deck (which is unsafe in terms of creating a "defensive" API), or use
 * an Optional<Card> as the return type (which I had coded up), but there still might be
 * cases where the user simply wants to check the deck without drawing a card, so this does
 * not solve the race condition.  So I went back to the classical boolean isDeckEmpty(),
 * dealOneCard() throws Exception approach.  Easy to understand for the caller and safe.
 * I could have added both atomic and non-atomic APIs to check and deal, but then this becomes
 * needlessly complex and confusing for a very simple abstraction.
 * 
 * All of that said, for concurrent use of the deck, the user would have to implement a fair
 * bit of synchronization, as even safely drawing a card does not ensure that another thread
 * won't change the deck contents. 
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
    public Card dealOneCard() throws EmptyDeckException {
        if (cardsRemaining == 0) {
            throw new EmptyDeckException("No cards remaining to deal!");
        }

        // Take the last card by decrementing the card count.
        return cards.get((cardsRemaining--) - 1);
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
        
        // Consider the cards remaining and portion of the deck that is active for
        // equality.  Thus we ignore the items that have already been dealt.
        Deck d = (Deck) obj;
        if (cardsRemaining != d.cardsRemaining) {
            return false;
        }
        
        // cardsRemaining are equal in both decks at this point.

        // If both are 0 length, no comparison needed (an optimization).
        if (cardsRemaining == 0)
            return true;
        
        return cardsRemaining == cards.size() ? cards.equals(d.cards)
                : cards.subList(0, cardsRemaining).equals(d.cards.subList(0, cardsRemaining));
    }

    @Override
    public int hashCode() {
        // According to the contract for hash code, two objects that are not equal()
        // can return distinct hash codes, but two objects that are equal() *must*
        // return the identical hash code.  Let's use similar logic to equals() given
        // these rules.
        return cardsRemaining == cards.size() ? cards.hashCode()
                : cards.subList(0, cardsRemaining).hashCode(); 
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
