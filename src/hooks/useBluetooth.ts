import { useState, useEffect, useCallback } from 'react';
import nativeModules from '../module/nativeModule';
import BackgroundTimer from 'react-native-background-timer';

const {
  startAdvertising,
  stopAdvertising,
} = nativeModules;

const useBluetooth = () => {
  const [isTransmitter, setIsTransmitter] = useState(false);
  const transmitterMs = 5000;

  // 광고 데이터 송신 시작 함수
  const onStartAdvertising = useCallback((hexString: string) => {
    setIsTransmitter(true);

    try {
      startAdvertising(hexString);
      
    } catch (error) {
      console.log(error);
    }
  }, []);

  // 지정한 시간 후에 BLE 광고를 종료한다.
  useEffect(() => {
    if (isTransmitter) {
      const timer = BackgroundTimer.setTimeout(() => {
        setIsTransmitter(false);

        // 광고 데이터 송신 종료 함수
        stopAdvertising();
      }, transmitterMs);

      return () => BackgroundTimer.clearTimeout(timer);
    }
  }, [isTransmitter, transmitterMs]);

  return {
    onStartAdvertising,
  };
};

export default useBluetooth;