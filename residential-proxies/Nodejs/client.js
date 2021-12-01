const settings = require('./settings');
const headers = require('./headers');

const axios = require('axios')
const rateLimit = require('axios-rate-limit')

const http = rateLimit(axios.create(), {
  maxRequests: settings.RequestsRate,
  perMilliseconds: 1000
})

const filesystem = require('./filesystem');

const apiUrl = `https://${settings.Username}:${settings.Password}@proxy.oxylabs.io/all`

const inSeconds = (ms) => ms * 1000;

module.exports = {
  fetchPage: async (proxy, url) => {
    parsedProxy = new URL(proxy);

    const proxyPort = parsedProxy.port;
    parsedProxy.port = '';

    let response = null;
    try {
      response = await http.get(url, {
        headers: headers.getRandomBrowserHeaders(),
        timeout: inSeconds(settings.Timeout),
        proxy: {
          host: parsedProxy.host,
          port: proxyPort,
          auth: { username: parsedProxy.username, password: parsedProxy.password }
        }
      });
    } catch (e) {
      return [null, e]
    }

    return [response, null]
  },
};

