package com.reactnativeble;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.facebook.react.PackageList;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint;
import com.facebook.soloader.SoLoader;
import java.util.List;

public class MainApplication extends Application implements ReactApplication {
  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      @SuppressWarnings("UnnecessaryLocalVariable")
      List<ReactPackage> packages = new PackageList(this).getPackages();
      // Packages that cannot be autolinked yet can be added manually here, for
      // example:
      packages.add(new MyAppPackage());
      return packages;
    }

    @Override
    protected String getJSMainModuleName() {
      return "index";
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }
  
  private static MainApplication instance;
  private BluetoothLeAdvertiser advertiser;

  // BLE Advertising 성공, 실패 시 Callback 함수
  private AdvertiseCallback advertiseCallback = new AdvertiseCallback() {
    @Override
    public void onStartSuccess(AdvertiseSettings settingsInEffect) {
      super.onStartSuccess(settingsInEffect);
      Log.d("MainApplication", "Advertising Start Success : " + settingsInEffect);
    }

    @Override
    public void onStartFailure(int errorCode) {
      super.onStartFailure(errorCode);
      Log.d("MainApplication", "Advertising Failed : " + errorCode);
    }
  };

  public static MainApplication getInstance() {
    return instance;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    instance = this;

    SoLoader.init(this, /* native exopackage */ false);
    if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
      // If you opted-in for the New Architecture, we load the native entry point for this app.
      DefaultNewArchitectureEntryPoint.load();
    }
    ReactNativeFlipper.initializeFlipper(this, getReactNativeHost().getReactInstanceManager());
  }

  // 받아온 manufaturer string data -> Byte 변환 함수
  public byte[] hexToBytes(String manufacturerData) {
    byte[] result = null;

    if (manufacturerData != null) {
      result = new byte[manufacturerData.length() / 2];

      for (int i = 0; i < result.length; i++) {
        result[i] = (byte) Integer.parseInt(manufacturerData.substring(2 * i, 2 * i + 2), 16);
      }
    }

    return result;
  }

  // 광고 데이터 송신 시작
  public void startAdvertising(String manufacturerData) {
    Log.i("MainApplication", "BLE ADVERTISING START");
    try {
      if (ActivityCompat.checkSelfPermission(this,
          Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
        return;
      }
      
      if (ActivityCompat.checkSelfPermission(this,
          Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
        return;
      }

      advertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();

      byte[] byteData = hexToBytes(manufacturerData);

      AdvertiseData ad_data = new AdvertiseData.Builder()
        .setIncludeDeviceName(true)
        .addManufacturerData(0xFFFF, byteData) // Use custom manufacturer data
        .build();

      AdvertiseSettings ad_settings = new AdvertiseSettings.Builder()
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setConnectable(false)
        .build();

      advertiser.startAdvertising(ad_settings, ad_data, advertiseCallback);
    } catch (Exception e) {
      // TODO: handle exception
      e.printStackTrace();
    }
  }

  // 광고 데이터 송신 중지
  public void stopAdvertising() {
    Log.i("MainApplication", "BLE ADVERTISING STOP");
    if (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    
    if (ActivityCompat.checkSelfPermission(this,
        Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
      return;
    }

    if (advertiser != null){
      advertiser.stopAdvertising(advertiseCallback);
      advertiser = null;
    }
  }
}