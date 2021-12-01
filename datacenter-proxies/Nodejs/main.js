(async() => {
  const { performance } = require('perf_hooks');
  const roundround = require('roundround');
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
  const proxiesRoundRobin = roundround(await client.fetchProxies());

  console.log('Gathering results...')
  let asyncJobs = [];
  for (let i = 0; i < urls.length; i++) {
    const proxy = proxyUtils.format(proxiesRoundRobin());
    asyncJobs.push(scraper.scrape(i+1, proxy, urls[i]))
  }

  await Promise.all(asyncJobs);

  const endTime = performance.now()

  console.log(`Script finished after ${((endTime - startTime)/1000).toFixed(2)} seconds`)
})();
