const axios = require('axios');
const fs = require('fs');

fs.readFile('keywords.json', (err, data) => {
  if (err) {
    console.log(err);
    return;
  }

  const payload = JSON.parse(data);

  axios.post('https://data.oxylabs.io/v1/queries/batch', payload, {
    auth: {
      username: 'user',
      password: 'pass1'
    },
    ContentType: "application/json"
  })
  .then(({ data }) => console.log(data))
  .catch(err => console.log(err))
});