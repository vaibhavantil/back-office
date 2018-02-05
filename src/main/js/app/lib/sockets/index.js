import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';
import * as dashboard from './dashboard';
import * as usersList from './users';
import * as chat from './chat';
import { getAuthToken } from '../checkAuth';

/* eslint-disable no-undef */
export const connect = () => {
    const token = getAuthToken();
    return new Promise((resolve, reject) => {
        const socket = new SockJS(`${config.ws.endpoint}?token=${token}`);
        const stompClient = Stomp.over(socket);
        stompClient.connect(
            {},
            () => {
                resolve(stompClient);
            },
            () => {
                reject(null);
            }
        );
    });
};

export const disconnect = (connection, subscription) => {
    if (connection) connection.disconnect();
    if (subscription) subscription.unsubscribe();
};

export const dashboardSubscribe = dashboard.subscribe;
export const usersListSubscribe = usersList.subscribe;
export const chatSubscribe = chat.subscribe;
export const chatReconnect = chat.reconnect;