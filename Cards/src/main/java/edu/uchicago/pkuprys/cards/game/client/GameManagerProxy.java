package edu.uchicago.pkuprys.cards.game.client;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.game.GameManager;
import edu.uchicago.pkuprys.cards.game.Player;
import edu.uchicago.pkuprys.cards.messaging.Messenger;
import edu.uchicago.pkuprys.cards.messaging.ObjectStreamMessenger;

public class GameManagerProxy implements GameManager {
    private Messenger messenger;

    public GameManagerProxy(InputStream in, OutputStream out) {
        this.messenger = new ObjectStreamMessenger(in, out);
    }

    @Override
    public void register(Player player) {
        if (player.getPosition() == -1) {
            player.setPosition((Integer) messenger.getMessage());
            messenger.sendMessage(player.getName());
        }
    }

    @Override
    public GameView getGameView() {
        return (GameView) messenger.getMessage();
    }

    @Override
    public void play(PlayerMove playerMove) {
        messenger.sendMessage(playerMove);
    }

    public void endGame(){
        messenger.close();
    }
}
