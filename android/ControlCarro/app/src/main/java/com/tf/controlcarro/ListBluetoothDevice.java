package com.tf.controlcarro;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class ListBluetoothDevice extends AppCompatActivity {

    private HashMap<String, String> devices;
    private BluetoothSocket mmSocket;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_bluetooth_device);

        devices = new HashMap<String, String>();
        String[] lst = listBluetoothDevices();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, lst);
        ListView listView = (ListView) findViewById(R.id.btDevList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String direction = devices.get(selectedItem);
                //Log.d(MainActivity.LOG_TAG, "Se seleccionó " + selectedItem + " - " + direction);
                Intent data = new Intent();
                data.putExtra(MainActivity.EXTRA, direction);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    private String[] listBluetoothDevices(){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        ArrayList<String> deviceNames = new ArrayList<String>();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
        }
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if(pairedDevices.size()>0){
            for(BluetoothDevice device: pairedDevices){
                String deviceName = device.getName();
                String deviceAddress = device.getAddress();
                devices.put(deviceName, deviceAddress);
                deviceNames.add(deviceName);
            }
        }
        String[] retValue = new String[deviceNames.size()];
        retValue = deviceNames.toArray(retValue);
        return retValue;
    }
}