package edu.uchicago.pkuprys.cards.android.host;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.game.GameManager;

public class WaitForPlayersTask extends AsyncTask<Integer, Void, List<BluetoothSocket>> {
    private static final String TAG = "edu.uchicago.pkuprys.cards.android.host.BluetoothHostThread";

    private ProgressDialog mProgressDialog;

    private HostGameActivity mHostGameActivity;

    private BluetoothServerSocket mServerSocket;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private List<BluetoothSocket> mClientSockets = new ArrayList<BluetoothSocket>();

    public WaitForPlayersTask(HostGameActivity hostGameActivity) {
        mHostGameActivity = hostGameActivity;
    }

    @Override
    protected List<BluetoothSocket> doInBackground(Integer... integers) {
        int numClients = integers[0];
        BluetoothServerSocket tmp = null;
        try {
            // GameManager.CARDS_UUID is the app's UUID string, also used by the client code
            tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(GameManager.CARDS, GameManager.CARDS_UUID);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        mServerSocket = tmp;

        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned
        while (mClientSockets.size() < numClients) {
            try {
                socket = mServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            // If a connection was accepted
            if (socket != null) {
                mClientSockets.add(socket);
            }
        }
        try {
            mServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return mClientSockets;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        DialogInterface.OnCancelListener ocl = new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                WaitForPlayersTask.this.cancel(true);
                closeSocket();
            }
        };
        mProgressDialog = ProgressDialog.show(mHostGameActivity, mHostGameActivity.getString(R.string.waiting_for_players),
                mHostGameActivity.getString(R.string.one_moment_please), true, true, ocl);
    }

    @Override
    protected void onPostExecute(List<BluetoothSocket> clientSockets) {
        super.onPostExecute(clientSockets);
        mProgressDialog.dismiss();
        mHostGameActivity.startGame(clientSockets);
    }

    // Will cancel the listening socket
    private void closeSocket() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
