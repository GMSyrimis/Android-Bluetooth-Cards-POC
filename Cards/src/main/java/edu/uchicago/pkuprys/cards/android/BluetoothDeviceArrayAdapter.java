package edu.uchicago.pkuprys.cards.android;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.uchicago.pkuprys.cards.R;

public class BluetoothDeviceArrayAdapter extends ArrayAdapter<BluetoothDevice> {
    public BluetoothDeviceArrayAdapter(Context context, List<BluetoothDevice> devices) {
        super(context, android.R.layout.simple_list_item_single_choice, devices);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView t = (TextView) ((Activity) getContext()).getLayoutInflater().inflate(R.layout.text_view_device, null);
        t.setText(getItem(position).getName());
        return t;
    }
}
