const settings = require('./settings');

module.exports = {
  createProxyByUrl: (proxyMap, url) => {
    country = null;
    urlParts = url.split(';')
    if (urlParts.length === 2) {
      url = urlParts[0];
      country = urlParts[1];
    }

    proxyAddress = proxyMap[settings.DefaultProxyIndexName]
    if (typeof proxyMap[country] !== 'undefined') {
      proxyAddress = proxyMap[country];
    }

    return [url,
      `http://customer-${settings.Username}:${settings.Password}@${proxyAddress}`,
    ]
  }
}
