package edu.uchicago.pkuprys.cards.domain;

import java.io.Serializable;
import java.util.Collection;


public class PlayerView implements Serializable {
    private Collection<PlayerState> states;
    private Hand hand;
    private int position;
    private PlayerMove playerMove;
    private String name;

    public PlayerView(Collection<PlayerState> states, Hand hand, int position, PlayerMove playerMove, String name) {
        this.position = position;
        this.states = states;
        this.hand = hand;
        this.playerMove = playerMove;
        this.name = name;
    }

    public Collection<PlayerState> getStates() {
        return states;
    }

    public int getPosition() {
        return position;
    }

    public Hand getHand() {
        return hand;
    }

    public PlayerMove getPlayerMove() {
        return playerMove;
    }

    public String getName() {
        return name;
    }
}
