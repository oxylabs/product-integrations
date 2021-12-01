const settings = require('./settings');

module.exports = {
  createProxyByUrl: (url) => {
    let country
    let parsedUrl = url

    urlParts = url.split(';')
    if (urlParts.length === 2) {
      parsedUrl = urlParts[0]
      country = urlParts[1]
    }

    return [
      parsedUrl,
      country,
      `http://${settings.Username}:${settings.Password}@${settings.ProxyAddress}`
    ]
  }
}
