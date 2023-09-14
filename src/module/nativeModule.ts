import { NativeModules } from "react-native";

export interface AndroidBeaconModule {
  startAdvertising: (hexString: string) => void;
  stopAdvertising: () => void;
};

export default {
  ...NativeModules.BeaconModule as AndroidBeaconModule, 
};