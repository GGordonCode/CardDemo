package org.gordon.cards;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DeckTest {
    
    @Test
    public void testDeckComplete() {
        List<Card> l = new ArrayList<Card>();
        try {
            Deck d = new Deck();
            while (!d.isEmpty()) {
                l.add(d.dealOneCard());
            }
            for (Card.Rank r : Card.Rank.values()) {
                for (Card.Suit s : Card.Suit.values()) {
                    Card c = new Card(r, s);
                    assertTrue("Deck is missing card " + c, l.contains(c));
                }
            }
        } catch (EmptyDeckException e) {
            fail("unexpected empty deck exception");
        }
    }
    
    @Test
    public void testShuffle() {
        List<Card> lu = new ArrayList<Card>();
        List<Card> ls = new ArrayList<Card>();
        try {
            Deck du = new Deck();
            while (!du.isEmpty()) {
                lu.add(du.dealOneCard());
            }
            Deck ds = new Deck();
            ds.shuffle();
            while (!ds.isEmpty()) {
                ls.add(ds.dealOneCard());
            }
            assertFalse("Decks did not get shuffled!", ls.equals(lu));
        } catch (EmptyDeckException e) {
            fail("unexpected empty deck exception");
        }
    }

    @Test
    public void testGetAllCardsNoRandomShuffle() {
        doTestGetAllCards(false);
    }

    @Test
    public void testGetAllCardsRandomShuffle() {
        doTestGetAllCards(true);
    }

    private void doTestGetAllCards(boolean randomShuffle) {
        System.out.println("------------------------------------");
        System.out.println("Test cards, random shuffles = " + randomShuffle);
        Deck deck = new Deck();
        deck.shuffle();
        Random generator = null;
        Set<Card> verifier = new HashSet<Card>();
        for (int i = 0; i < 55; i++) {
            if (randomShuffle) {
                if (generator == null) {
                    generator = new Random();
                }
                if (generator.nextInt(3) == 0) {
                    deck.shuffle();
                    System.out.println("shuffle at card " + i);
                }
            }

            assertTrue("isEmpty() returned unexpcted value for card " + i,
                    deck.isEmpty() == (i >= 52));

            try {
                Card c = deck.dealOneCard();
                if (i >= 52) {
                    fail("Expected exception on card " + i);
                }
                System.out.println("Card " + i + ": " + c);
                if (c != null) {
                    if (verifier.contains(c)) {
                        fail("Card " + c + " is duplicated in deck!");
                    } else {
                        verifier.add(c);
                    }
                }
            } catch (EmptyDeckException e) {
                if (i < 52) {
                    fail("Card " + i + " was unexpectedly empty!");
                }
            }
        }
        assertEquals("Unexpected deck size: " + verifier.size(), verifier.size(), 52);
    }
}
