const axios = require('axios');

const payload = {
  "source": "universal",
  "url": "http://ip.oxylabs.io",
}

axios.post('https://data.oxylabs.io/v1/queries', payload, {
  auth: {
    username: 'user',
    password: 'pass1'
  },
  ContentType: "application/json"
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))