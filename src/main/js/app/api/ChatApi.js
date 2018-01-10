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
            id: 0,
            name: 'Test User 0',
            newMessage: false
        },
        {
            id: 1,
            name: 'First User',
            newMessage: false
        },
        {
            id: 2,
            name: 'Second User',
            newMessage: false
        },
        {
            id: 3,
            name: 'Thrid User',
            newMessage: false
        },
        {
            id: 4,
            name: 'Tester User 4',
            newMessage: false
        },
        {
            id: 5,
            name: 'Test User 5',
            newMessage: false
        },
        {
            id: 6,
            name: 'Tester User 6',
            newMessage: false
        },
        {
            id: 7,
            name: 'Test User 7',
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
