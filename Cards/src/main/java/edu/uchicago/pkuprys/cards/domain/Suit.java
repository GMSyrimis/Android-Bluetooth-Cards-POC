package edu.uchicago.pkuprys.cards.domain;

public enum Suit {
    CLUBS(0, "Clubs"),
    SPADES(1, "Spades"),
    HEARTS(2, "Hearts"),
    DIAMONDS(3, "Diamonds");

    private int id;
    private String name;

    private Suit(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Suit valueByName(String name) {
        return Suit.valueOf(name.toUpperCase());
    }
}
