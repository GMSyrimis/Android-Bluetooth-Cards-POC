package edu.uchicago.pkuprys.cards.domain;


import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {
    private static final String OF = " of ";
    private Suit suit;
    private Rank rank;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Card(String card) {
        String[] cardParts = card.split(OF);
        this.rank = Rank.valueByName(cardParts[0]);
        this.suit = Suit.valueByName(cardParts[1]);
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    @Override
    public String toString() {
        return rank.getName() + OF + suit.getName();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rank == null) ? 0 : rank.hashCode());
        result = prime * result + ((suit == null) ? 0 : suit.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Card other = (Card) obj;
        if (rank != other.rank)
            return false;
        if (suit != other.suit)
            return false;
        return true;
    }

    @Override
    public int compareTo(Card other) {
        return rank.compareTo(other.rank) == 0 ? suit.compareTo(other.suit) : rank.compareTo(other.rank);
    }

    public String getDrawableName() {
        return "_" + Integer.toString(rank.getId() * 4 + suit.getId() + 1);
    }
}
