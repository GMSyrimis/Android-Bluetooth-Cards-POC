package edu.uchicago.pkuprys.cards.android;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.uchicago.pkuprys.cards.R;
import edu.uchicago.pkuprys.cards.android.client.JoinGameActivity;

public class FindDevicesFragment extends DialogFragment {
    private ArrayAdapter<BluetoothDevice> mDevicesAdapter;
    private BluetoothDevice mHostDevice;
    private JoinGameActivity mJoinGameActivity;

    public FindDevicesFragment(JoinGameActivity joinGameActivity) {
        mJoinGameActivity = joinGameActivity;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if (mDevicesAdapter.getPosition(device) < 0) {
                    mDevicesAdapter.add(device);
                }
            }
        }
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ListView listView = (ListView) getActivity().getLayoutInflater().inflate(R.layout.dialog_find_devices, null);
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mHostDevice = (BluetoothDevice) adapterView.getItemAtPosition(i);
                view.setSelected(true);
            }
        });
        BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
        List<BluetoothDevice> devices = findPairedDevices(bta);
        mDevicesAdapter = new BluetoothDeviceArrayAdapter(getActivity(), devices);
        bta.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);
        listView.setAdapter(mDevicesAdapter);
        DialogClickListener dcl = new DialogClickListener();
        return new AlertDialog.Builder(getActivity()).setView(listView).setTitle(getResources().getString(R.string.find_devices_title))
                .setPositiveButton(android.R.string.ok, dcl).setNegativeButton(android.R.string.cancel, dcl).create();
    }

    private List<BluetoothDevice> findPairedDevices(BluetoothAdapter bta) {
        List<BluetoothDevice> devices = new ArrayList();
        Set<BluetoothDevice> pairedDevices = bta.getBondedDevices();
        devices.addAll(pairedDevices);
        return devices;
    }

    private class DialogClickListener implements DialogInterface.OnClickListener {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE:
                    mJoinGameActivity.connectToHost(mHostDevice);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    getActivity().finish();
                    break;
            }
        }
    }
}
