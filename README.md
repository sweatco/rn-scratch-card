# rn-scratch-card

React Native Scratch Card which temporarily hides content from a user

![Scratch Sample](https://github.com/sweatco/rn-scratch-card/raw/main/demo.gif)

Check out it on [dribble](https://dribbble.com/shots/17396594-Sweatcoin-Scratch-The-Prize-Feature-Lottery-Style).

## Installation

```sh
yarn add rn-scratch-card
```

[![https://nodei.co/npm/rn-scratch-card.png?downloads=true&downloadRank=true&stars=true](https://nodei.co/npm/rn-scratch-card.png?downloads=true&downloadRank=true&stars=true)](https://www.npmjs.com/package/rn-scratch-card)

## Usage

```js
import { ScratchCard } from 'rn-scratch-card'

// ...
<ScratchCard
  source={require('./scratch_foreground.png')}
  brushWidth={50}
  onScratch={handleScratch}
  style={styles.scratch_card}
/>
```

## Example project setup

```sh
cd rn-scratch-card
yarn install
cd example
yarn install
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
