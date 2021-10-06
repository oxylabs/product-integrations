const axios = require('axios');

axios.get('http://data.oxylabs.io/v1/queries/12345678900987654321/results', {
  auth: {
    username: 'user',
    password: 'pass1'
  },
  ContentType: "application/json"
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))