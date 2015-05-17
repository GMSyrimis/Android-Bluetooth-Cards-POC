package edu.uchicago.pkuprys.cards.android.client;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.android.CardsActivity;
import edu.uchicago.pkuprys.cards.android.FindDevicesFragment;
import edu.uchicago.pkuprys.cards.android.GamePlayActivity;
import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerState;
import edu.uchicago.pkuprys.cards.domain.PlayerView;
import edu.uchicago.pkuprys.cards.game.GameManager;
import edu.uchicago.pkuprys.cards.game.Player;
import edu.uchicago.pkuprys.cards.game.client.GameManagerProxy;

public class JoinGameActivity extends FragmentActivity {
    private static final String DIALOG_FIND_DEVICES = "edu.uchicago.pkuprys.cards.android.devices.FIND_DEVICES_DIALOG";
    private static final String TAG = "edu.uchicago.pkuprys.cards.android.devices.JoinGameActivity";

    private GameManager mGameManager;
    private Player mPlayer;
    private String mName;
    private boolean mGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getIntent().getStringExtra(CardsActivity.PLAYER_NAME);
        setContentView(R.layout.activity_join_game);
        FragmentManager fm = getSupportFragmentManager();
        FindDevicesFragment dialog = new FindDevicesFragment(this);
        dialog.show(fm, DIALOG_FIND_DEVICES);
    }

    public void connectToHost(BluetoothDevice bluetoothDevice) {
        new ConnectToHostTask(this).execute(bluetoothDevice);
    }

    public void startGame(BluetoothSocket hostSocket) {
        InputStream in = null;
        OutputStream out = null;
        try {
            in = hostSocket.getInputStream();
            out = hostSocket.getOutputStream();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        mPlayer = new Player();
        mPlayer.setName(mName);
        mGameManager = new GameManagerProxy(in, out);
        mGameManager.register(mPlayer);
        new GetGameViewTask().execute();
    }

    private void executeTurn(GameView gameView) {
        mPlayer.update(gameView);
        for (PlayerView pv : gameView.getPlayerViews()) {
            if (pv.getPosition() == mPlayer.getPosition()) {
                if (pv.getStates().contains(PlayerState.UP)) {
                    Intent intent = new Intent(this, GamePlayActivity.class);
                    intent.putExtra(CardsActivity.GAME_VIEW, gameView);
                    intent.putExtra(Player.POSITION, mPlayer.getPosition());
                    startActivityForResult(intent, 0);
                }
            }
        }
        if (mPlayer.getHand().getCards().size() == 1) {
            mGameOver = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Card card = (Card) data.getSerializableExtra(GamePlayActivity.CARD_PLAYED);
        mPlayer.getHand().remove(card);
        mGameManager.play(new PlayerMove(mPlayer.getPosition(), card, true));
        if (mGameOver) {
            endGame();
        }
        new GetGameViewTask().execute();
    }

    private void endGame() {
        ((GameManagerProxy) mGameManager).endGame();
        finish();
    }

    private class GetGameViewTask extends AsyncTask<String, Void, GameView> {
        ProgressDialog mProgressDialog;

        @Override
        protected GameView doInBackground(String... strings) {
            GameView view = null;
            try {
                view = mGameManager.getGameView();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return view;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            DialogInterface.OnCancelListener ocl = new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    GetGameViewTask.this.cancel(true);
                    endGame();
                }
            };
            mProgressDialog = ProgressDialog.show(JoinGameActivity.this, JoinGameActivity.this.getString(R.string.waiting_for_moves),
                    JoinGameActivity.this.getString(R.string.one_moment_please), true, true, ocl);
        }

        @Override
        protected void onPostExecute(GameView gameView) {
            try {
                super.onPostExecute(gameView);
                mProgressDialog.dismiss();
                executeTurn(gameView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
