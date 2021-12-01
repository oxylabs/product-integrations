(async() => {
  const { performance } = require('perf_hooks');
  const settings = require('./settings');
  const filesystem = require('./filesystem');
  const client = require('./client');
  const util = require('./util');
  const scraper = require('./scraper');
  const proxyUtils = require('./proxy');

  const startTime = performance.now();

  console.log('Reading from the list...');
  const urls = filesystem.readLines(settings.UrlListName)

  console.log('Retrieving proxy list...')

  console.log('Gathering results...')
  let asyncJobs = [];
  for (let i = 0; i < urls.length; i++) {
    [parsedUrl, country, formattedProxy] = proxyUtils.createProxyByUrl(urls[i]);
    asyncJobs.push(scraper.scrape(i+1, formattedProxy, parsedUrl, country))
  }

  await Promise.all(asyncJobs);

  const endTime = performance.now()

  console.log(`Script finished after ${((endTime - startTime)/1000).toFixed(2)} seconds`)
})();
