package edu.uchicago.pkuprys.cards.domain;

import java.io.Serializable;
import java.util.Collection;


public class GameView implements Serializable {
    private Collection<PlayerView> playerViews;

    public GameView(Collection<PlayerView> pvs) {
        this.playerViews = pvs;
    }

    public Collection<PlayerView> getPlayerViews() {
        return playerViews;
    }
}
