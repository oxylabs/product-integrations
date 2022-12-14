import axios, {AxiosRequestConfig} from 'axios';

// If you wish to get content in HTML you can delete parser_type and parse parameters
const payload = {
    query: [
        'kettle',
        'fridge',
        'microwave'
    ],
    source: 'amazon_search',
    geo_location: '10005',
    parse: 'true',
};

const config: AxiosRequestConfig = {
    auth: {
        username: 'user', // Don't forget to fill in user credentials
        password: 'pass1'
    },
    headers: {
        'Content-Type': 'application/json',
        'Accept-Encoding': 'gzip,deflate,compress',
    }
}

axios.post('https://data.oxylabs.io/v1/queries/batch', payload, config)
    .then(({data}) => console.log(data))
    .catch(err => console.log(err))
