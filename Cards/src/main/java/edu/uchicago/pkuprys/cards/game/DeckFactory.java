package edu.uchicago.pkuprys.cards.game;

import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.Deck;
import edu.uchicago.pkuprys.cards.domain.Rank;
import edu.uchicago.pkuprys.cards.domain.Suit;

public class DeckFactory {
    public static Deck getDefaultDeck() {
        Deck deck = new Deck();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                deck.add(new Card(rank, suit));
            }
        }
        return deck;
    }

    public static Deck getFourAceDeck() {
        Deck deck = new Deck();
        for (Suit suit : Suit.values()) {
                deck.add(new Card(Rank.ACE, suit));
        }
        return deck;
    }
}
