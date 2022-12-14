import axios from 'axios';
import express, {Express, Request, Response} from 'express';
import * as bodyParser from 'body-parser';

const credentials = {
    auth: {
        username: 'user',
        password: 'pass1'
    },
    headers: {
        'Content-Type': 'application/json',
        'Accept-Encoding': 'gzip,deflate,compress',
    },
}

const app: Express = express();
const port = 8080;

app.use(bodyParser.json());

app.post('/job_listener', (req: Request, res: Response) => {
    const links = req.body._links
    const resultLinks = links.filter(link => link.rel === 'results');

    // create an array of promises from filtered links
    const requests = resultLinks.map(({href}) => axios.get(href, {...credentials}));

    Promise.all(requests).then(values => {
        console.log(values);
        res.status(200).json({status: 'ok '});
    })
        .catch(errors => console.log(errors));
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`)
});
