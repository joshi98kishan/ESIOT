package com.example.android.esiot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    Button startBt, stopBt;
    ListView listOfBtDevice;
    BluetoothAdapter btAdapter;
    Intent btEnablingIntent;
    int requestCodeForEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startBt=(Button)findViewById(R.id.start_bt);
        stopBt=(Button)findViewById(R.id.stop_bt);
        listOfBtDevice=(ListView) findViewById(R.id.showing_list);
        btAdapter=BluetoothAdapter.getDefaultAdapter();
        btEnablingIntent=new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        requestCodeForEnable=1;

        bluetoothOnMethod();
        bluetoothOFFMethod();
    }

    private void bluetoothOFFMethod() {
        stopBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btAdapter.isEnabled()){
                    btAdapter.disable();
                    listOfBtDevice.setAdapter(null);
                    Toast.makeText(getApplicationContext(), "Bluetooth is disabled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bluetoothOnMethod() {
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btAdapter == null) {
                    Toast.makeText(getApplicationContext(), "Blutooth does not support on this device", Toast.LENGTH_LONG).show();
                } else {
                    if (btAdapter.isEnabled()) {
                        Toast.makeText(MainActivity.this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show();
                    }else{
                        startActivityForResult(btEnablingIntent, requestCodeForEnable);
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==requestCodeForEnable){
            if(resultCode==RESULT_OK){
                Toast.makeText(getApplicationContext(), "Bluetooth is Enable", Toast.LENGTH_LONG).show();

                Set<BluetoothDevice> bt=btAdapter.getBondedDevices();
                String[] strings=new String[bt.size()];
                int index=0;

                if(bt.size()>0){
                    for(BluetoothDevice device:bt){
                        strings[index]=device.getName();
                        index++;
                    }

                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,strings);
                    listOfBtDevice.setAdapter(adapter);
                }

            }else if(resultCode==RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth Enabling Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
}
