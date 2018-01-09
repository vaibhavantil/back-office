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
        url: config.asset.get.url,
        method: config.asset.get.method,
        headers: {
            ...axiosInstance.headers,
            [config.tokenHeaderName]: token,
        }
    });
};

const update = async (id, state, token) => {
    return await axiosInstance.request({
        url: `${config.asset.update.url}/${id}`,
        method: config.asset.update.method,
        headers: {
            ...axiosInstance.headers,
            [config.tokenHeaderName]: token,
        },
        data: {
            id,
            state
        }
    });
};

export default { get, update };
