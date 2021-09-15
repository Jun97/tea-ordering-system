var external;
try {
	external = require('./webpack.external');
} catch (e) {
    if (e.code !== 'MODULE_NOT_FOUND') {
        throw e;
    }
    external = function (isProduction) {
    	return {
    		title: 'Untitled',
    		baseUrl: '/',
    		host: 'localhost',
    		port: 30001,
    	}
    }
}
module.exports = external;