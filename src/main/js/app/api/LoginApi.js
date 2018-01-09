import config from './config';
import axios from 'axios';

const axiosInstance = axios.create({
    baseURL: config.baseUrl,
    timeout: 10000,
    withCredentials: false,
    headers: {
        Accept: 'application/json',
        'Content-Type': 'application/json',
    }
});

const login = async (email, password) => {
    return await axiosInstance.request({
        method: config.login.login.method,
        url: config.login.login.url,
        data: {
            email,
            password
        }
    });
};

const logout = async () => {
    return await axiosInstance.request({
        method: config.login.logout.method,
        url: config.asset.logout.url
    });
};

export default { login, logout };
