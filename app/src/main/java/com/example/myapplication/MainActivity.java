package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BluetoothManager bluetoothManager;
    BluetoothAdapter bluetoothAdapter;
    private ScanSettings scanSettings;
    ScanSettings.Builder scanSettingsBuilder;
    private Handler scanHandler = new Handler();
    private List scanFilters = new ArrayList();

    private Button startScanButton;
    private Button stopScanButton;
    BluetoothLeScanner scanner;
    ScanFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startScanButton = findViewById(R.id.startScanBtn);
        startScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBluethoothScan();
                startForegroundService();
            }
        });
        stopScanButton = findViewById(R.id.stopScanBtn);
        stopScanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (scanner != null){
                    scanner.stopScan(scanCallback);
                }
            }
        });
    }

    private void startForegroundService() {
        Log.i("MainActivity","startForegroundService called!");
        Context context = getApplicationContext();
        Intent intent = new Intent(MainActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        }
    }

    private void startBluethoothScan() {
        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        scanSettings = scanSettingsBuilder.build();
        filter = new ScanFilter.Builder().setDeviceAddress("FC:58:FA:FF:6C:0E").build();
        scanFilters.add(filter);

        scanHandler.post(scanRunnable);

    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            scanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            scanner.startScan(scanFilters, scanSettings,scanCallback);
        }
    };

    public ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.i("MainActivity","onScanResult called!");
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
            Log.i("MainActivity","onBatchScanResults called!");
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.i("MainActivity","onScanFailed called!");
        }
    };
}