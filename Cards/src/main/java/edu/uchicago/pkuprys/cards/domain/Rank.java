package edu.uchicago.pkuprys.cards.domain;

public enum Rank {
    ACE(0, "Ace"),
    KING(1, "King"),
    QUEEN(2, "Queen"),
    JACK(3, "Jack"),
    TEN(4, "Ten"),
    NINE(5, "Nine"),
    EIGHT(6, "Eight"),
    SEVEN(7, "Seven"),
    SIX(8, "Six"),
    FIVE(9, "Five"),
    FOUR(10, "Four"),
    THREE(11, "Three"),
    TWO(12, "Two");

    private int id;
    private String name;

    private Rank(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Rank valueByName(String name) {
        return Rank.valueOf(name.toUpperCase());
    }
}
