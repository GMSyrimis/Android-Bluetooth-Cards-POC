package edu.uchicago.pkuprys.cards.game;

import java.util.HashSet;
import java.util.Set;

import edu.uchicago.pkuprys.cards.android.host.HostGameActivity;
import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.Hand;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerState;
import edu.uchicago.pkuprys.cards.domain.PlayerView;

public class Player {
    public static final String POSITION = "edu.uchicago.pkuprys.cards.game.POSITION";

    private int position;
    private String name;
    private Hand hand;
    private Set<PlayerState> states;

    public Player() {
        this.states = new HashSet<PlayerState>();
        this.hand = new Hand();
        this.position = -1;
    }

    public void deal(Card card) {
        hand.add(card);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void update(GameView gameView) {
        if (hand.isEmpty()) {
            for (PlayerView pv : gameView.getPlayerViews()) {
                if (position == pv.getPosition()) {
                    hand.addAll(pv.getHand().getCards());
                    break;
                }
            }
        }
    }

    public int getPosition() {
        return position;
    }

    public boolean isDealer() {
        return states.contains(PlayerState.DEALER);
    }

    public void addState(PlayerState state) {
        states.add(state);
    }

    public void removeState(PlayerState state) {
        states.remove(state);
    }

    public Set<PlayerState> getStates() {
        return states;
    }

    public Hand getHand() {
        return hand;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PlayerMove getMove() {
        return null;
    }

    public void getMove(HostGameActivity hostGameActivity, GameView gameView) {
        hostGameActivity.getHostMove(gameView);
    }
}
