# rn-scratch-card

React Native Scratch Card which temporarily hides content from a user

![Scratch Sample](https://github.com/sweatco/rn-scratch-card/raw/main/demo.gif)

Check out it on [dribble](https://dribbble.com/shots/17396594-Sweatcoin-Scratch-The-Prize-Feature-Lottery-Style).

## Installation

```sh
yarn add @sweatco/rn-scratch-card
```

Published on npm as [`@sweatco/rn-scratch-card`](https://www.npmjs.com/package/@sweatco/rn-scratch-card); the unscoped `rn-scratch-card` stops at 1.2.4.

## Usage

```js
import { ScratchCard } from '@sweatco/rn-scratch-card'

// ...
<ScratchCard
  source={require('./scratch_foreground.png')}
  brushWidth={50}
  onScratch={handleScratch}
  style={styles.scratch_card}
/>
```

`source` accepts a bundled asset (`require(...)`) or a remote image (`{ uri: 'https://…' }`). Remote images load asynchronously and are cached in memory.

## Example project setup

```sh
cd rn-scratch-card
yarn install
cd example
yarn install
yarn run react-native run-ios
```

If you are launching project under iOS, please, also remember to

```sh
cd ios
pod install
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
