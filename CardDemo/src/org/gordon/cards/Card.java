package org.gordon.cards;

/**
 * The card abstraction holds a distinct rank and suit for each card.
 */
public class Card {

    /**
     * The enumeration of card ranks, along with a per-type printable string.
     * There are cuter ways to do this, by not assigning a value to TWO through
     * TEN, and using Integer.toString(this.ordinal() + 2) for the string value,
     * for those but it's a little inconsistent to assign a null pointer to some
     * members, ordinal() is really intended for internal use, and the extra
     * overhead of the numeric strings is not huge.
     */
    public static enum Rank {
        TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"),
        NINE("9"), TEN("10"), JACK("Jack"), QUEEN("Queen"), KING("King"), ACE("Ace");

        private final String name;

        private Rank(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * The enumeration of card suits, along with a per-type printable string.
     */
    public static enum Suit {
        HEARTS("Hearts"), DIAMONDS("Diamonds"), SPADES("Spades"), CLUBS("Clubs");

        private final String name;

        private Suit(String s) {
            name = s;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * The rank for the current card.
     */
    private Rank rank;

    /**
     * The suit for the current card.
     */
    private Suit suit;

    /**
     * Creates a new instance of a card.
     * @param rank The rank, from the Enum @{code Card.Rank}
     * @param suit The rank, from the Enum @{code Card.Suit}
     */
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }
    
    /**
     * Get the card's rank (ordinal 2-Ace) from the {@code Card.Rank Enum}.
     * @return The card's rank.
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Get the card's suit from the {@code Card.Suit Enum}.
     * @return The card's suit.
     */
    public Suit getSuit() {
        return suit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        // A subclass of Card is not equal to a Card, as a
        // subclass could have additional fields or semantics.
        // I mean, we could start looking at all the declared
        // fields and try to get it right, but let's not go there!
        if (obj == null || !getClass().equals(obj.getClass())) {
            return false;
        }
        Card c = (Card) obj;
        return rank.equals(c.rank) && suit.equals(c.suit);
    }

    @Override
    public int hashCode() {
        return (rank.ordinal() << 8) | suit.ordinal();
    }

    /**
     * Creates a human-friendly descriptive string for this @{code Card}
     */
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
