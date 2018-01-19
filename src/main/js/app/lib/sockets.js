import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';
import * as types from './messageTypes';

const connectError = { stompClient: null, subscription: null };

/* eslint-disable no-undef */
const responseHandler = (actions, response) => {
    const parsedRes = JSON.parse(response.body);
    const data = parsedRes.payload;

    if (parsedRes.type === 'ERROR') {
        actions.errorReceived(data);
        return;
    }
    actions.messageReceived(data.messages);
};

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

export const subscribe = (actions, clientId, stompClient) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                config.ws.messages + clientId,
                responseHandler.bind(this, actions)
            );
            stompClient.send(config.ws.history + clientId);
            return { stompClient, subscription };
        } catch (error) {
            return connectError;
        }
    } else {
        return connectError;
    }
};

export const disconnect = (connection, subscription) => {
    if (connection) connection.disconnect();
    if (subscription) subscription.unsubscribe();
};

export const reconnect = (actions, clientId) => {
    return new Promise((resolve, reject) => {
        connect()
            .then(connection => {
                const { stompClient, subscription } = subscribe(
                    actions,
                    clientId,
                    connection
                );
                resolve({ stompClient, subscription });
            })
            .catch(() => {
                reject(connectError);
            });
    });
};

export const updateMessageBody = (content, message, type) => {
    const updated = { ...content };
    switch (type) {
        case types.PARAGRAPH:
            updated.body.imageURL = message[type];
            break;
        case types.NUMBER:
            updated.body.number = message[type];
            break;
        case types.SINGLE_SELECT:
            updated.body.choices = [...message[type]];
            break;
        case types.MULTIPLE_SELECT:
            updated.body.choices = [...message[type]];
            break;
        case types.DATE:
            updated.body.date = message[type];
            break;
        case types.AUDIO:
        case types.VIDEO:
        case types.PHOTO:
        case types.HERO:
            updated.body.URL = message[type];
            break;
        default:
            break;
    }

    return JSON.stringify(updated);
};
