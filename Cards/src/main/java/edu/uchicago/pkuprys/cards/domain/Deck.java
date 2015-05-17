package edu.uchicago.pkuprys.cards.domain;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<Card>();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public Card pullRandom() {
        int index = (int) Math.floor(cards.size() * Math.random());
        return cards.remove(index);
    }

    public int size() {
        return cards.size();
    }

    public List<Card> getCards() {
        return cards;
    }
}
