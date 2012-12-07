package com.example.bluetoothmetry;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import java.util.ArrayList;

public class MainActivity extends Activity {

	Button mBtScan = null;
	boolean bIsScanning = false; 
	BluetoothAdapter mBtAdapter = null;
	TextView mTvResult = null;
	private final int REQUEST_ENABLE_BT = 1;
    private ArrayList<BluetoothDevice> mArrayAdapter;
    BroadcastReceiver mReceiver;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mBtScan = (Button) findViewById(R.id.btScan);
		mTvResult = (TextView) findViewById(R.id.tvResult);

		//enable Bluetooth 
		mBtAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBtAdapter == null) {
			System.exit(-1);
		}
		if (!mBtAdapter.isEnabled()) {
			
		    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		}

		// Create a BroadcastReceiver for ACTION_FOUND
		mReceiver = new BroadcastReceiver() {

			public void onReceive(Context context, Intent intent) {
		        String action = intent.getAction();
		        // When discovery finds a device
		        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
		            // Get the BluetoothDevice object from the Intent
		        	BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		        	short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

		        	String str = device.getName() + " | " + device.getAddress() + " | " + rssi+ "dBm\n";
		            // Add the name and address to an array adapter to show in a ListView
//		            mArrayAdapter.add(device);
//		            mTvResult.setText( str );
		            mTvResult.append( str );
		        }
//		        if(BluetoothDevice.ACTION_FOUND.equals(action)) {
//		            short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI,Short.MIN_VALUE);
//		            Toast.makeText(getApplicationContext(),"  RSSI: " + rssi + "dBm", Toast.LENGTH_SHORT).show();
//		        }
		        if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
		        	mBtScan.setText("Scan");
		        }
			}
		};
		// Register the BroadcastReceiver
		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy

		
		mBtScan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	bIsScanning = !bIsScanning;
                // Perform action on click
            	if(bIsScanning) {
            		mBtScan.setText("Cancel");
            		mTvResult.setText("");
            		mBtAdapter.startDiscovery();
            	}else{
            		mBtAdapter.cancelDiscovery();
            		mBtScan.setText("Scan");
            	}
            }
        });
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mReceiver);
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode !=  RESULT_OK){
			System.exit(-1);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}
