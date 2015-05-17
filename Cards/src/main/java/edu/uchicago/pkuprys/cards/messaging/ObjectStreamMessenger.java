package edu.uchicago.pkuprys.cards.messaging;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class ObjectStreamMessenger implements Messenger {
    public static final String TAG = "edu.uchicago.pkuprys.cards.messaging.ObjectStreamMessenger";
    private ObjectInputStream mIn;
    private ObjectOutputStream mOut;

    public ObjectStreamMessenger(InputStream is, OutputStream out) {
        try {
            mOut = new ObjectOutputStream(out);
            mOut.flush();
            mIn = new ObjectInputStream(is);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void sendMessage(Object message) {
        try {
            mOut.reset();
            mOut.writeObject(message);
            mOut.flush();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public Object getMessage() {
        Object message = null;
        try {
            message = mIn.readObject();
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
        return message;
    }

    @Override
    public void finalize() {
        close();
    }

    @Override
    public void close() {
        try {
            mIn.close();
            mOut.close();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
