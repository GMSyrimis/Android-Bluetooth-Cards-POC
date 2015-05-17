package edu.uchicago.pkuprys.cards.android;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.android.client.JoinGameActivity;
import edu.uchicago.pkuprys.cards.android.host.HostGameActivity;

public class CardsActivity extends Activity {
    private static final int REQUEST_ENABLE_BT = 1;
    public static final String GAME_VIEW = "edu.uchicago.pkuprys.cards.android.GAME_VIEW";
    public static final String PLAYER_NAME = "edu.uchicago.pkuprys.cards.android.PLAYER_NAME";


    private Button mHostGame;
    private Button mJoinGame;
    private boolean mIsHost;
    private EditText mEditName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        mHostGame = (Button) findViewById(R.id.host_game_button);
        mHostGame.setOnClickListener(new GameStartListener(true));

        mJoinGame = (Button) findViewById(R.id.join_game_button);
        mJoinGame.setOnClickListener(new GameStartListener(false));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, getString(R.string.bluetooth_enabled), Toast.LENGTH_SHORT).show();
                    initGame();
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, getString(R.string.bluetooth_not_enabled_text), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void checkBluetooth() {
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        if (null == bta) {
            //no bluetooth, cannot continue
            Toast.makeText(this, getString(R.string.no_bluetooth_text), Toast.LENGTH_LONG).show();
            finish();
        }
        if (!bta.isEnabled()) {
            //have user enable bluetooth
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_ENABLE_BT);
        } else {
            initGame();
        }
    }

    private void initGame() {
        mEditName = (EditText) findViewById(R.id.editName);
        String name = mEditName.getText().toString();
        Class<? extends Activity> initGameClass = mIsHost ? HostGameActivity.class : JoinGameActivity.class;
        Intent initGame = new Intent(this, initGameClass);
        initGame.putExtra(PLAYER_NAME, name);

        startActivity(initGame);
    }

    private class GameStartListener implements View.OnClickListener {
        private boolean isHost;

        public GameStartListener(boolean isHost) {
            this.isHost = isHost;
        }

        @Override
        public void onClick(View view) {
            CardsActivity.this.mIsHost = isHost;
            checkBluetooth();
        }
    }
}
