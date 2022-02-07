import {
  requireNativeComponent,
  UIManager,
  Platform,
  ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package 'rn-scratch-card' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo managed workflow\n';

type RnScratchCardProps = {
  color: string;
  style: ViewStyle;
};

const ComponentName = 'RnScratchCardView';

export const RnScratchCardView =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<RnScratchCardProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };
