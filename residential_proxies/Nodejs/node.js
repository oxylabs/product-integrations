#!/usr/bin/env node
require('request-promise')({
   url: 'https://ip.oxylabs.io',
   proxy: 'http://customer-USER:PASS@pr.oxylabs.io:7777'
 }).then(function(data){ console.log(data); }, function(err){ console.error(err); });