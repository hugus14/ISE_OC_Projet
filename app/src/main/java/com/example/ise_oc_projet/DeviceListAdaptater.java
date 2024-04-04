package com.example.ise_oc_projet;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class DeviceListAdaptater extends ArrayAdapter<BluetoothDevice> implements View.OnClickListener {

    private ArrayList<BluetoothDevice> deviceSet;
    private BluetoothAdapter mBtAdapter;
    Context mContext;
    private final String TAG = "DeviceListAdaptater";

    // View lookup cache
    private static class ViewHolder {
        TextView device_address_TV;
        TextView device_name_TV;
    }

    public DeviceListAdaptater(Context context, BluetoothAdapter mBtAdapter) {
        super(context, R.layout.device_item);
        this.deviceSet = new ArrayList<>();
        this.mContext = context;
        this.mBtAdapter = mBtAdapter;

    }

    public boolean doesDeviceExist(BluetoothDevice device){
        Object find = deviceSet.stream()
                .filter(seenDevice -> seenDevice.getAddress().equals(device.getAddress()))
                .findAny()
                .orElse(null);

        Log.println(Log.INFO, "DeviceListAdaptater", "current new device: "+device.getAddress());
        Log.println(Log.INFO, "DeviceListAdaptater", "current already exist ? : "+(find != null));
        deviceSet.forEach(seenDevice ->
                        Log.println(Log.INFO, "DeviceListAdaptater", "device stream: "+seenDevice.getAddress())
                );

        return find != null;
    }

    @Override
    public void onClick(View v) {
/*
        Log.println(Log.INFO, TAG, "staring click on item");
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        BluetoothDevice clicked_device = (BluetoothDevice) object;

        ConnectThread BT_connexion = new ConnectThread(mBtAdapter,clicked_device);
        BT_connexion.start();

 */

    }

    @Override
    public void add(@Nullable BluetoothDevice object) {
        BluetoothDevice device = object;

        @SuppressLint("MissingPermission") String device_name = device.getName();
        String device_addr = device.getAddress();
        if(device_name != null){
            Log.println(Log.INFO, "DeviceListAdaptater", "device added : "+device_name +" - "+device_addr );
            super.add(device);
            deviceSet.add(device);
        }

    }

    private int lastPosition = -1;

    @SuppressLint("MissingPermission")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BluetoothDevice device = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.device_item, parent, false);
            viewHolder.device_address_TV = (TextView) convertView.findViewById(R.id.device_address);
            viewHolder.device_name_TV = (TextView) convertView.findViewById(R.id.device_name);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        lastPosition = position;

        String device_name = device.getName();
        if(device_name == null){ device_name = "#no readable#";}

        viewHolder.device_name_TV.setText(device_name);
        viewHolder.device_address_TV.setText(device.getAddress());
        //viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }
}

