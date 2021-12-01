const settings = require('./settings');

module.exports = {
  createProxyByUrl: (url) => {
    urlParts = url.split(';')
    if (urlParts.length === 2) {
      return [
        urlParts[0],
        `http://customer-${settings.Username}-cc-${urlParts[1]}:${settings.Password}@${settings.ProxyAddress}`]
    }

    return [url,
      `http://customer-${settings.Username}:${settings.Password}@${settings.ProxyAddress}`,
    ]
  }
}
