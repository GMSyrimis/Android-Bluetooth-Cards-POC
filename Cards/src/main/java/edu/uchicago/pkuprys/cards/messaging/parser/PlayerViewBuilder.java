package edu.uchicago.pkuprys.cards.messaging.parser;

import java.util.ArrayList;
import java.util.Collection;

import edu.uchicago.pkuprys.cards.domain.Hand;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerState;
import edu.uchicago.pkuprys.cards.domain.PlayerView;

public class PlayerViewBuilder {
    private Collection<PlayerState> states = new ArrayList<PlayerState>();
    private Hand hand = new Hand();
    private int position;
    private PlayerMove playerMove;
    private String name;

    public PlayerViewBuilder playerStates(Collection<PlayerState> states) {
        this.states = states;
        return this;
    }

    public PlayerViewBuilder hand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public PlayerViewBuilder position(int position) {
        this.position = position;
        return this;
    }

    public PlayerViewBuilder playerMove(PlayerMove playerMove) {
        this.playerMove = playerMove;
        return this;
    }

    public PlayerViewBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PlayerView build() {
        return new PlayerView(states, hand, position, playerMove, name);
    }
}
