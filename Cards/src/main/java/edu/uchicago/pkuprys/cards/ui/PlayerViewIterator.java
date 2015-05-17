package edu.uchicago.pkuprys.cards.ui;

import java.util.Collection;
import java.util.Iterator;

import edu.uchicago.pkuprys.cards.domain.PlayerView;

public class PlayerViewIterator implements Iterator<PlayerView> {
    //currently game size is static at 4 players
    private static final int[] DISPLAY_ORDER_DIFF = {2, 1, 3, 0};
    private Collection<PlayerView> pvs;
    private int player;
    private boolean hasNext = false;
    private PlayerView current;
    private int currentIndex = 0;

    public PlayerViewIterator(Collection<PlayerView> pvs, int player) {
        this.pvs = pvs;
        this.player = player;
        if (null != pvs && pvs.size() > 0) {
            hasNext = true;
            determineNext();
        }
    }

    @Override
    public boolean hasNext() {
        return hasNext && currentIndex < pvs.size();
    }

    @Override
    public PlayerView next() {
        PlayerView next = current;
        determineNext();
        return next;
    }

    private void determineNext() {
        if (currentIndex >= pvs.size()) {
            return;
        }
        for (PlayerView pv : pvs) {
            if (pv.getPosition() == (player + DISPLAY_ORDER_DIFF[currentIndex]) % pvs.size()) {
                current = pv;
                currentIndex++;
                break;
            }
        }
    }

    @Override
    public void remove() {
        //Do Nothing
    }
}
