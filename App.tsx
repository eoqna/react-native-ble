import { TouchableOpacity, View, Text } from 'react-native';
import useBluetooth from './src/hooks/useBluetooth';

const ViewStyle = {
  flex: 1,
  justifyContent: 'center',
  alignItems: 'center',
};

const ButtonStyle = {
  backgroundColor: 'red',
  padding: 10,
  border: '1px solid black',
  borderRadius: 5,
};

const TextStyle = {
  color: 'white',
};

const App = () => {
  const { onStartAdvertising } = useBluetooth();
  
  const bleAdvertising = () => {
    const manufacturerData = "1111111111";

    onStartAdvertising(manufacturerData);
  };

  return (
    <View
      style={ViewStyle}
    >
      <TouchableOpacity
        onPress={bleAdvertising}
        style={ButtonStyle}
      >
        <Text style={TextStyle}>BLE 광고</Text>
      </TouchableOpacity>
    </View>
  );
};

export default App;