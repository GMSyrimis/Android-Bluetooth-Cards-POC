package edu.uchicago.pkuprys.cards.domain;


public enum GameState {
    PRE(0, "Pre"),
    ACTIVE(1, "Active"),
    OVER(1, "Over");

    private int id;
    private String name;

    private GameState(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static GameState valueByName(String name) {
        return GameState.valueOf(name.toUpperCase());
    }
}
