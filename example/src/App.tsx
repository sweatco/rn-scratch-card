import * as React from 'react';

import { Image, StyleSheet, View } from 'react-native';
import { ScratchCard } from 'rn-scratch-card';

export default function App() {
  return (
    <View style={styles.container}>
      <Image
        source={require('./scratch_background.png')}
        style={styles.background_view}
      />
      <ScratchCard
        source={require('./scratch_foreground.png')}
        brushWidth={50}
        onScratch={handleScratch}
        style={styles.scratch_card}
      />
    </View>
  );

  function handleScratch(scratchPercentage: number) {
    console.log(scratchPercentage);
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 16,
  },
  background_view: {
    position: 'absolute',
    width: 400,
    height: 400,
    backgroundColor: 'transparent',
    alignSelf: 'center',
    borderRadius: 16,
  },
  scratch_card: {
    width: 400,
    height: 400,
    backgroundColor: 'transparent',
  },
});
