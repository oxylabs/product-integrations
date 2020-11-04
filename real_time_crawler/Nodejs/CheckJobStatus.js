const axios = require('axios');

axios.get('https://data.oxylabs.io/v1/queries/12345678900987654321', {
  auth: {
    username: 'user',
    password: 'pass1'
  },
  ContentType: "application/json"
})
.then(({ data }) => console.log(data))
.catch(err => console.log(err))