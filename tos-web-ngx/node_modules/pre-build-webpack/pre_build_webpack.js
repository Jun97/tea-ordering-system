/**
 * @module WebpackPreBuildPlugin
 */

/**
 * @constructor
 * @param {preBuildCallback} callback - will be called before build.
 */
function WebpackPreBuildPlugin(callback) {
  this.callback = callback;
}

/**
 * @callback preBuildCallback
 * @param {object} stats - webpack stats object
 */

/**
 * @param {object} compiler
 */
WebpackPreBuildPlugin.prototype.apply = function(compiler) {
  compiler.plugin('compile', this.callback);
};

module.exports = WebpackPreBuildPlugin;

