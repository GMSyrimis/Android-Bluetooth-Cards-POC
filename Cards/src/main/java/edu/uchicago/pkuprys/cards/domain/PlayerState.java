package edu.uchicago.pkuprys.cards.domain;


public enum PlayerState {
    DEALER(0, "Dealer"),
    UP(1, "Up"),
    WAITING(2, "Waiting"),
    HOST(3, "Host");

    private int id;
    private String name;

    private PlayerState(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static PlayerState valueByName(String name) {
        return PlayerState.valueOf(name.toUpperCase());
    }
}
