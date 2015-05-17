package edu.uchicago.pkuprys.cards.android.host;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.android.CardsActivity;
import edu.uchicago.pkuprys.cards.android.GamePlayActivity;
import edu.uchicago.pkuprys.cards.domain.Card;
import edu.uchicago.pkuprys.cards.domain.GameView;
import edu.uchicago.pkuprys.cards.domain.PlayerMove;
import edu.uchicago.pkuprys.cards.domain.PlayerState;
import edu.uchicago.pkuprys.cards.domain.PlayerView;
import edu.uchicago.pkuprys.cards.game.GameManagerImpl;
import edu.uchicago.pkuprys.cards.game.Player;
import edu.uchicago.pkuprys.cards.messaging.PlayerProxy;

public class HostGameActivity extends Activity {
    private static final String TAG = "edu.uchicago.pkuprys.cards.android.host.HostGameActivity";

    private Spinner mSpinner;
    private Button mButton;
    private GameManagerImpl mGameManager;
    private Player mHostPlayer;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_game);

        mName = getIntent().getStringExtra(CardsActivity.PLAYER_NAME);
        mSpinner = (Spinner) findViewById(R.id.num_players_spinner);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.num_players_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(spinnerAdapter);
        mSpinner.setSelection(1);

        mButton = (Button) findViewById(R.id.start_game_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
                startActivity(discoverableIntent);
                new WaitForPlayersTask(HostGameActivity.this).execute(HostGameActivity.this.mSpinner.getSelectedItemPosition());
            }
        });
    }

    public void startGame(List<BluetoothSocket> clientSockets) {
        mGameManager = (GameManagerImpl) GameManagerImpl.getInstance();
        mGameManager.initGame(HostGameActivity.this.mSpinner.getSelectedItemPosition() + 1);
        for (int i = 0; i < clientSockets.size(); i++) {
            PlayerProxy pp = null;
            try {
                InputStream in = clientSockets.get(i).getInputStream();
                OutputStream out = clientSockets.get(i).getOutputStream();
                pp = new PlayerProxy(in, out);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            mGameManager.register(pp);
            pp.initialize();
        }

        mHostPlayer = new Player();
        mHostPlayer.setName(mName);
        mHostPlayer.addState(PlayerState.HOST);
        mGameManager.register(mHostPlayer);
        mGameManager.setHostGameActivity(this);

        takeTurn();
    }

    private void takeTurn() {
        mGameManager.executeTurn();
    }

    public void getHostMove(GameView gameView) {
        mHostPlayer.update(gameView);
        for (PlayerView pv : gameView.getPlayerViews()) {
            if (pv.getPosition() == mHostPlayer.getPosition()) {
                if (pv.getStates().contains(PlayerState.UP)) {
                    Intent intent = new Intent(this, GamePlayActivity.class);
                    intent.putExtra(CardsActivity.GAME_VIEW, gameView);
                    intent.putExtra(Player.POSITION, mHostPlayer.getPosition());
                    startActivityForResult(intent, 0);
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Card card = (Card) data.getSerializableExtra(GamePlayActivity.CARD_PLAYED);
        mGameManager.play(new PlayerMove(mHostPlayer.getPosition(), card, true));
        takeTurn();
    }
}