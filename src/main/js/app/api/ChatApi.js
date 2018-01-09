import axios from 'axios';
import config from './config';

/* eslint-disable no-undef */
const axiosInstance = axios.create({
    baseURL: config.baseUrl,
    timeout: 10000,
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    }
});

const get = async (token) => {
    return await axiosInstance.request({
        url: config.chats.get.url,
        method: config.chats.get.method,
        headers: {
            ...axiosInstance.headers,
            [config.tokenHeaderName]: token,
        }
    });
};

export default { get };
