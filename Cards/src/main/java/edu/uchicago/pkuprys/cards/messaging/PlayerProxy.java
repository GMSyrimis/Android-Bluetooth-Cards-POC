package edu.uchicago.pkuprys.cards.messaging;

import java.io.InputStream;
import java.io.OutputStream;

import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.game.Player;

public class PlayerProxy extends Player {
    private Messenger messenger;

    public PlayerProxy(InputStream in, OutputStream out) {
        super();
        this.messenger = new ObjectStreamMessenger(in, out);
    }

    @Override
    public void update(GameView gameView) {
        messenger.sendMessage(gameView);
    }

    @Override
    public PlayerMove getMove() {
        return (PlayerMove) messenger.getMessage();
    }

    public void initialize() {
        messenger.sendMessage(new Integer(getPosition()));
        setName((String) messenger.getMessage());
    }

    public void endGame(){
        messenger.close();
    }
}
