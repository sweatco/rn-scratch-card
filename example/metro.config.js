const path = require('path');
const exclusionList =
  require('metro-config/private/defaults/exclusionList').default;
const escape = require('escape-string-regexp');
const pak = require('../package.json');
const { getDefaultConfig, mergeConfig } = require('@react-native/metro-config');

const root = path.resolve(__dirname, '..');

const modules = Object.keys({
  ...pak.peerDependencies,
});

/**
 * @type {import('@react-native/metro-config').MetroConfig}
 */
const config = {
  watchFolders: [root],

  // Keep a single copy of peer dependencies: the example's, not the library's.
  resolver: {
    blockList: exclusionList(
      modules.map(
        (m) => new RegExp(`^${escape(path.join(root, 'node_modules', m))}\\/.*$`)
      )
    ),

    extraNodeModules: modules.reduce((acc, name) => {
      acc[name] = path.join(__dirname, 'node_modules', name);
      return acc;
    }, {}),
  },
};

module.exports = mergeConfig(getDefaultConfig(__dirname), config);
