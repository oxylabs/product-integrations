const settings = require('./settings');

module.exports = {
  format: (proxyUrl) => {
    return `http://${settings.Username}:${settings.Password}@${proxyUrl}`;
  }
}
