package edu.uchicago.pkuprys.cards.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hand implements Serializable {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<Card>();
    }

    public List<Card> getCards() {
        return cards;
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void addAll(List<Card> newCards) {
        cards.addAll(newCards);
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
