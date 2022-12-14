import axios, {AxiosRequestConfig} from 'axios';

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

axios.get('https://data.oxylabs.io/v1/info/callbacker_ips', config)
    .then(({data}) => console.log(data))
    .catch(err => console.log(err))
