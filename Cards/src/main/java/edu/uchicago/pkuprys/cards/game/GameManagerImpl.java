package edu.uchicago.pkuprys.cards.game;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.android.host.HostGameActivity;
import edu.uchicago.pkuprys.cards.domain.GameState;
import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerState;
import edu.uchicago.pkuprys.cards.domain.PlayerView;
import edu.uchicago.pkuprys.cards.messaging.PlayerProxy;
import edu.uchicago.pkuprys.cards.messaging.parser.PlayerViewBuilder;

//singleton
public class GameManagerImpl implements GameManager {
    private static GameManager gameManager;

    private PlayersIterator mPlayersIterator;

    public void setHostGameActivity(HostGameActivity hostGameActivity) {
        mHostGameActivity = hostGameActivity;
    }

    private HostGameActivity mHostGameActivity;

    public static GameManager getInstance() {
        if (null == gameManager) {
            gameManager = new GameManagerImpl();
        }
        return gameManager;
    }

    private Game game;
    private Map<Integer, PlayerMove> playerMoves;

    private GameManagerImpl() {
        playerMoves = new HashMap<Integer, PlayerMove>();
    }

    public void initGame(int numPlayers){
        game = new Game(numPlayers);
    }

    @Override
    public void register(Player player) {
        player.setPosition(game.addPlayer(player));
        setState(player);
    }

    private void setState(Player player) {
        if (player.getPosition() == 0) {
            player.addState(PlayerState.DEALER);
        }
        player.addState(PlayerState.WAITING);
    }

    @Override
    public void play(PlayerMove playerMove) {
        Player player = game.getPlayers().get(playerMove.getPlayer());
        player.getHand().getCards().remove(playerMove.getCard());
        playerMoves.put(playerMove.getPlayer(), playerMove);
        game.playCard(playerMove.getCard());
        toWaiting(player);
        if (player.isDealer()) {
            for (PlayerMove move : playerMoves.values()) {
                move.setCurrent(false);
            }
        }
    }

    @Override
    public GameView getGameView() {
        Collection<PlayerView> pvs = new ArrayList<PlayerView>();
        for (Player player : game.getPlayers()) {
            PlayerViewBuilder pvb = new PlayerViewBuilder();
            pvb.hand(player.getHand());
            pvb.playerStates(player.getStates());
            pvb.position(player.getPosition());
            pvb.playerMove(playerMoves.get(player.getPosition()));
            pvb.name(player.getName());
            pvs.add(pvb.build());
        }
        return new GameView(pvs);
    }

    public void executeTurn() {
        if (null == mPlayersIterator) {
            mPlayersIterator = new PlayersIterator(game.getPlayers());
        }
        if (!GameState.OVER.equals(game.getGameState()) && mPlayersIterator.hasNext()) {
            Player player = mPlayersIterator.next();
            toUp(player);
            player.update(getGameView());
            if (player.getStates().contains(PlayerState.HOST)) {
                player.getMove(mHostGameActivity, getGameView());
            } else {
                new GetRemotePlayerMoveTask().execute(player);
            }
        } else {
            endGame();
        }
    }

    public void toUp(Player player) {
        player.removeState(PlayerState.WAITING);
        player.addState(PlayerState.UP);
    }

    public void toWaiting(Player player) {
        player.removeState(PlayerState.UP);
        player.addState(PlayerState.WAITING);
    }

    private class GetRemotePlayerMoveTask extends AsyncTask<Player, Void, PlayerMove> {
        ProgressDialog mProgressDialog;

        @Override
        protected PlayerMove doInBackground(Player... players) {
            return players[0].getMove();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DialogInterface.OnCancelListener ocl = new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    GetRemotePlayerMoveTask.this.cancel(true);
                    GameManagerImpl.this.endGame();
                }
            };
            mProgressDialog = ProgressDialog.show(mHostGameActivity, mHostGameActivity.getString(R.string.waiting_for_moves),
                    mHostGameActivity.getString(R.string.one_moment_please), true, true, ocl);
        }

        @Override
        protected void onPostExecute(PlayerMove playerMove) {
            super.onPostExecute(playerMove);
            play(playerMove);
            mProgressDialog.dismiss();
            executeTurn();
        }
    }

    private void endGame(){
        for(Player player : game.getPlayers()){
            if(!player.getStates().contains(PlayerState.HOST)){
                ((PlayerProxy) player).endGame();
            }
        }
        mHostGameActivity.finish();
    }
}



