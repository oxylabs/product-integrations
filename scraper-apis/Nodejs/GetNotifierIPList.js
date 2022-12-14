const axios = require('axios');

axios.get('https://data.oxylabs.io/v1/info/callbacker_ips', {
  auth: {
    username: 'user',
    password: 'pass1'
  },
  headers: {
    'Content-Type': 'application/json',
    'Accept-Encoding': 'gzip,deflate,compress',
  },
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))
