const client = require('./client');
const settings = require('./settings');
const filesystem = require('./filesystem');

module.exports = {
  scrape: async (position, proxy, url) => {
    let response = null;
    let retry = 0;
    do {
      [response, err] = await client.fetchPage(proxy, url);
      if (response !== null && response.status !== 200) {
        await filesystem.writeErrorToFile(`${url} - Response code ${response.status}`)
      } else if (err !== null) {
        await filesystem.writeErrorToFile(`${url} - Response error ${err.message}`)
        filesystem.writeErrorToStdout(`${url} failed with error ${err.message}`)
      }

      if (response !== null && response.status === 200) {
        await filesystem.writeSuccessToFile(position, response.data)
        break
      }

      retry += 1;
    } while (retry < settings.RetriesNum);
  }
};
