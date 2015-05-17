package edu.uchicago.pkuprys.cards.domain;

import java.io.Serializable;

public class PlayerMove implements Serializable {
    private Integer player;
    private Card card;
    private Boolean current;

    public PlayerMove(Integer player, Card card, Boolean current) {
        this.player = player;
        this.card = card;
        this.current = current;
    }

    public Integer getPlayer() {
        return player;
    }

    public Card getCard() {
        return card;
    }

    public Boolean isCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }
}
