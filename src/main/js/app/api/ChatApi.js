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
        url: config.users.get.url,
        method: config.users.get.method,
        headers: {
            [config.tokenHeaderName]: token
        }
    });
};

const search = async (token, queryString) => {
    return await axiosInstance.request({
        url: config.users.search.url,
        method: config.users.search.method,
        params: {
            q: queryString
        },
        headers: {
            [config.tokenHeaderName]: token
        }
    });
};

const getUser = async (token, userId) => {
    return await axiosInstance.request({
        url: `${config.users.get.url}/${userId}`,
        method: config.users.get.method,
        headers: {
            [config.tokenHeaderName]: token
        }
    });
};

export default { get, search, getUser };
