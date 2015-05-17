package edu.uchicago.pkuprys.cards.game;

import java.util.List;
import java.util.UUID;

import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;

public interface GameManager {
    public static final UUID CARDS_UUID = UUID.fromString("adf9eabc-5267-4b1d-b36b-f666869e8747");
    public static final String CARDS = "edu.uchicago.pkuprys.cards";

    public void register(Player player);

    public GameView getGameView();

    public void play(PlayerMove playerMove);
}
