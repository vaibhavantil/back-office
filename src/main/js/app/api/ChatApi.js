import axios from 'axios';
import config from './config';

/* eslint-disable */
const axiosInstance = axios.create({
    baseURL: config.baseUrl,
    timeout: 10000,
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json'
    }
});

const get = async token => {
    return [
        {
            id: 12,
            name: 'Test User',
            newMessage: false
        },
        {
            id: 2,
            name: 'Test User 2',
            newMessage: false
        }
    ];
    /*  return await axiosInstance.request({
        url: config.chats.get.url,
        method: config.chats.get.method,
        headers: {
            ...axiosInstance.headers,
            [config.tokenHeaderName]: token,
        }
    }); */
};

export default { get };
