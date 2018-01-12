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

const get = async token => {
    return await axiosInstance.request({
        url: config.chats.get.url,
        method: config.chats.get.method,
        headers: {
            [config.tokenHeaderName]: token
        }
    });
};

const search = async (token, queryString) => {
    return await axiosInstance.request({
        url: config.chats.search.url,
        method: config.chats.search.method,
        params: {
            search: queryString
        },
        header: {
            [config.tokenHeaderName]: token
        }
    });
};

export default { get, search };
