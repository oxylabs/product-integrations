const settings = require('./settings');

module.exports = {
  createProxyByUrl: (url) => {
    return [url,
      `http://customer-${settings.Username}:${settings.Password}@${settings.ProxyAddress}`,
    ]
  }
}
