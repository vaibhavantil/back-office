import axios from 'axios';
import config from './config';

const axiosInstance = axios.create({
    baseURL: config.baseUrl,
    timeout: 10000,
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

export default async (token, conf, data, id, params) =>
    await axiosInstance.request({
        url: `${conf.url}${id ? '/' + id : ''}`,
        method: conf.method,
        headers: {
            [config.tokenHeaderName]: token
        },
        data,
        params
    });
