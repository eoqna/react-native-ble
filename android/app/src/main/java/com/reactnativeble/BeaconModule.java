package com.reactnativeble;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import android.util.Log;

public class BeaconModule extends ReactContextBaseJavaModule {
  private ReactApplicationContext reactContext;

  public BeaconModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  // React-Native 로 내보내는 명칭
  @Override
  public String getName() {
    return "BeaconModule";
  }

  // 광고 데이터 송신 시작
  @ReactMethod
  public void startAdvertising(String manufaturerData) {
    Log.d("BeaconModule", "StartAdvertising");
    MainApplication.getInstance().startAdvertising(manufaturerData);
  }

  // 광고 데이터 송신 중지
  @ReactMethod
  public void stopAdvertising() {
    Log.d("BLE", "StopAdvertising");
    MainApplication.getInstance().stopAdvertising();
  }
}