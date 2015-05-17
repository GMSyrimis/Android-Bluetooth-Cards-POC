package edu.uchicago.pkuprys.cards.android.client;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;

import edu.uchicago.pkuprys.cards.game.GameManager;

public class ConnectToHostTask extends AsyncTask<BluetoothDevice, Void, BluetoothSocket> {
    private static final String TAG = "edu.uchicago.pkuprys.cards.android.client.ConnectToHostTask";
    private BluetoothSocket mSocket;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private JoinGameActivity mJoinGameActivity;

    public ConnectToHostTask(JoinGameActivity joinGameActivity) {
        mJoinGameActivity = joinGameActivity;
    }

    @Override
    protected BluetoothSocket doInBackground(BluetoothDevice... bluetoothDevices) {
        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            mSocket = bluetoothDevices[0].createRfcommSocketToServiceRecord(GameManager.CARDS_UUID);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mSocket.connect();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
            // Unable to connect; close the socket and get out
            try {
                mSocket.close();
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }
        return mSocket;
    }

    @Override
    protected void onPostExecute(BluetoothSocket bluetoothSocket) {
        mJoinGameActivity.startGame(bluetoothSocket);
    }
}

