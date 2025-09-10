const TsconfigPathsPlugin = require("tsconfig-paths-webpack-plugin");

module.exports = function override(config) {
  if (!config.resolve) {
    config.resolve = {};
  }
  if (!config.resolve.plugins) {
    config.resolve.plugins = [];
  }
  config.resolve.plugins.push(new TsconfigPathsPlugin());
  return config;
};
