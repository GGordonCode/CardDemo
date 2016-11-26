# CardDemo
Card Deck in Java.  Compiled with JDK 8, should be compatible with JDK 7.

Main classes are Card and Deck.  There are embedded public Enums in Card for suit and rank.  There is a comprehensive JUnit test called DeckTest that will verify the correctness of the code.

Usage:

Deck d = new Deck();

d.shuffle();

try {

    d.isEmpty();

} catch (EmptyDeckException e) {

    ...

}

Card c = d.dealOneCard();

Card.Suit suit = c.getSuit();

Card.Rank rank = c.getRank();

System.out.println("I drew card: " + c);

See comments in code for design justifications.
