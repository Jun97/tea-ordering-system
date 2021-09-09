# pre-build-webpack

[Webpack](http://webpack.github.io/) plugin that gives ability to add callback
before build.

Inspired by [on-build-webpack](https://github.com/kossnocorp/on-build-webpack), which provides a hook for the end of the build.

## Installation

```
npm install --save-dev pre-build-webpack
```

## Usage

In config file:

``` javascript
var WebpackPreBuildPlugin = require('pre-build-webpack');

// ...
  module: {
    plugins: [
      new WebpackPreBuildPlugin(function(stats) {
        // Do whatever you want before build starts...
      }),
    ]
  },
// ...
```

