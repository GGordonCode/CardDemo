# CardDemo
Card Deck implemented in Java.  Compiled with JDK 8, should be compatible with JDK 7.

Main classes are Card and Deck.  There are embedded public Enums in Card for suit and rank.  There is a comprehensive JUnit test called DeckTest that will verify the correctness of the code.

Usage:

try {

    Deck d = new Deck();

    d.shuffle();

    if (!d.isEmpty()) {

        Card c = d.dealOneCard();

        Card.Suit suit = c.getSuit();

        Card.Rank rank = c.getRank();

        System.out.println("I drew card: " + c);
    
    }

} catch (EmptyDeckException e) {

    ...

}


See comments in code for design justifications.
