package com.red_folder.phonegap.plugin.backgroundservice.sample;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;

import com.red_folder.phonegap.plugin.backgroundservice.BackgroundService;

public class MyService extends BackgroundService {
	
	private final static String TAG = MyService.class.getSimpleName();

	private boolean isRunning;
	private Context context;
	private Thread bThread;

	// The bluetooth adapter on the device.
	private BluetoothAdapter mBluetoothAdapter;
	private BluetoothLeScanner mBluetoothLeScanner;

	private List<BluetoothDevice> mBluetoothDevices;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 5000;

	private final ParcelUuid uuid = ParcelUuid.fromString("eebe6650-f0b1-2091-674c-c0f073e22fd5");

	@Override
	protected JSONObject doWork() {
		JSONObject result = new JSONObject();
		
		try {
			final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			mBluetoothAdapter = bluetoothManager.getAdapter();
			mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();

			mBluetoothDevices = new ArrayList<>();

			ScanFilter scanFilter = new ScanFilter.Builder().setServiceUuid(uuid).build();
			List<ScanFilter> scanFilterList = new ArrayList<>();
			scanFilterList.add(scanFilter);

			ScanSettings scanSettings = new ScanSettings.Builder()
					.setScanMode(SCAN_MODE_LOW_POWER).build();

			mBluetoothLeScanner.startScan(scanFilterList, scanSettings, mLeScanCallback);

			processScanResults(mBluetoothDevices);
		} catch (JSONException e) {
		}
		
		return result;	
	}

	private void processScanResults(List<BluetoothDevice> devices) {
		if (devices.size() != 0) {
			//devices.get(0).connectGatt(context, true, bluetoothGattCallback);

			mBluetoothDevices = new ArrayList<>();
		}
	}

	private ScanCallback mLeScanCallback = new ScanCallback() {
		@Override
		public void onScanResult(int callbackType, ScanResult result) {
			BluetoothDevice device = result.getDevice();
			mBluetoothDevices.add(result.getDevice());
			Log.d(TAG, result.toString());
		}

		@Override
		public void onBatchScanResults(List<ScanResult> results) {
			Log.d(TAG, String.format("Size of the list is %s", results.size()));
		}
	};

	@Override
	protected JSONObject getConfig() {
		JSONObject result = new JSONObject();
		
		try {
			result.put("HelloTo", this.mHelloTo);
		} catch (JSONException e) {
		}
		
		return result;
	}

	@Override
	protected void setConfig(JSONObject config) {
		try {
			if (config.has("HelloTo"))
				this.mHelloTo = config.getString("HelloTo");
		} catch (JSONException e) {
		}
		
	}     

	@Override
	protected JSONObject initialiseLatestResult() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onTimerEnabled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTimerDisabled() {
		// TODO Auto-generated method stub
		
	}


}
