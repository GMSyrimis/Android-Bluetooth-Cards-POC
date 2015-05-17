package edu.uchicago.pkuprys.cards.game;

import java.util.Collection;
import java.util.Iterator;

public class PlayersIterator implements Iterator<Player> {
    private Collection<Player> players;
    private Player current;
    //since we don't want to stop until all hands of the round have been played
    //we don't set this to false if there is more than one player
    private boolean hasNext = false;
    private boolean foundFirst = false;

    public PlayersIterator(Collection<Player> players) {
        this.players = players;
        if (null != players && players.size() > 0) {
            hasNext = true;
            determineNext();
        }
    }

    private void determineNext() {
        if (foundFirst) {
            int currentPosition = current.getPosition();
            for (Player player : players) {
                if (player.getPosition() == (currentPosition + 1) % players.size()) {
                    current = player;
                }
            }
        } else {
            int dealer = findDealerPosition();
            if (dealer == -1) {
                throw new IllegalStateException("No dealer has been assigned. Game cannot commence.");
            }
            for (Player player : players) {
                if (player.getPosition() == (dealer + 1) % players.size()) {
                    foundFirst = true;
                    current = player;
                }
            }
        }
    }

    private int findDealerPosition() {
        int dealer = -1;
        for (Player player : players) {
            if (player.isDealer()) {
                dealer = player.getPosition();
            }
        }
        return dealer;
    }

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public Player next() {
        Player next = current;
        determineNext();
        return next;
    }

    @Override
    public void remove() {
        //Do nothing
    }
}
