#!/usr/bin/env node
require('request-promise')({
   url: 'https://ip.oxylabs.io',
   proxy: 'http://username:pass@PROXY:PORT'
 }).then(function(data){ console.log(data); }, function(err){ console.error(err); });
