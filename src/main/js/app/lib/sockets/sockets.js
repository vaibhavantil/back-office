import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';
import * as dashboard from './dashboardSockets';
import * as usersList from './usersListSockets';
import * as chat from './chatSockets';

/* eslint-disable no-undef */
export const connect = () => {
    const token = JSON.parse(localStorage.getItem('token'));
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