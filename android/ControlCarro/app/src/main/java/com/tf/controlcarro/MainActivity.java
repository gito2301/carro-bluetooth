package com.tf.controlcarro;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA = "btAddress";
    public static final String LOG_TAG = "BluetoothControlTAG";

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String NAME = "btName";

    private final int REQUEST_ENABLE_BT = 123;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothSocket mmSocket;
    private Intent btIntent;
    private ActivityResultLauncher<Intent> mGetContent;
    private OutputStream ostream;
    private AcceptThread acceptThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkBluetooth();

        btIntent = new Intent(this, ListBluetoothDevice.class);
        mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        String address = result.getData().getStringExtra(EXTRA);
                        Toast.makeText(MainActivity.this, "Trying to connect to device", Toast.LENGTH_SHORT).show();
                        BluetoothDevice dev = bluetoothAdapter.getRemoteDevice(address);
                        Log.d(LOG_TAG, "Device obtained: " + dev.toString());
                        //acceptThread = new AcceptThread();
                        //acceptThread.start();
                        if(mmSocket != null){
                            try {
                                mmSocket.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            mmSocket = null;
                        }
                        try{
                            mmSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
                            bluetoothAdapter.cancelDiscovery();
                            mmSocket.connect();
                            ostream = mmSocket.getOutputStream();
                        }catch(IOException e){
                            Log.d(LOG_TAG, "Socket Creation Failed", e);
                           try {
                               mmSocket.close();
                           }catch (IOException f){
                               Log.d(LOG_TAG, "Could not close the client socket.");
                           }
                           return;
                        }

                    }
                });
    }

    public void orderStop(View view) {
        if(mmSocket != null && mmSocket.isConnected() ){
            byte[] a = {'s'};
            try {
                ostream.write(a);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Could not close the client socket.");
            }
        }
    }

    public void orderUp(View view) {
        if(mmSocket != null && mmSocket.isConnected() ){
            byte[] a = {'u'};
            try {
                ostream.write(a);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Could not close the client socket.");
            }
        }
    }

    public void orderDown(View view) {
        if(mmSocket != null && mmSocket.isConnected() ){
            byte[] a = {'d'};
            try {
                ostream.write(a);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Could not close the client socket.");
            }
        }
    }

    public void orderRight(View view) {
        if(mmSocket != null && mmSocket.isConnected() ){
            byte[] a = {'r'};
            try {
                ostream.write(a);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Could not close the client socket.");
            }
        }
    }

    public void orderLeft(View view) {
        if(mmSocket != null && mmSocket.isConnected() ){
            byte[] a = {'l'};
            try {
                ostream.write(a);
            } catch (IOException e) {
                Log.d(LOG_TAG, "Could not close the client socket.");
            }
        }
    }

    public void showListBluetooth(View view){
        mGetContent.launch(btIntent);
    }

    private void checkBluetooth() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(this, "Este dispositivo no cuenta con bluetooth", Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth activo requerido.", Toast.LENGTH_LONG).show();
                return;
            }
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try{
                tmp = bluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME, MY_UUID);
            } catch (IOException e){
                Log.e(LOG_TAG,"Socket's listen() method failed", e);
            }
            mmServerSocket = tmp;
        }

        public void run(){
            BluetoothSocket socket = null;
            while(true){
                try{
                    socket = mmServerSocket.accept();
                }catch (IOException e){
                    Log.e(LOG_TAG, "Socket's accept() method failed", e);
                    break;
                }
                if(socket!=null){
                    try{
                        mmSocket = socket;
                        mmSocket.connect();
                        ostream = mmSocket.getOutputStream();
                        mmServerSocket.close();
                    }catch(IOException e){
                        Log.d(LOG_TAG, "Socket Creation Failed", e);
                        try {
                            mmSocket.close();
                        }catch (IOException f){
                            Log.d(LOG_TAG, "Could not close the client socket.");
                        }
                    }
                    break;
                }
            }
        }

        public void cancel(){
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Could not close the connect socket.", e);
            }
        }
    }
}