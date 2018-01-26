import config from 'app/api/config';
import * as types from './messageTypes';
import { connect } from './sockets';

const connectError = { stompClient: null, subscription: null };

const responseHandler = (actions, response) => {
    const parsedRes = JSON.parse(response.body);
    const data = parsedRes.payload;

    if (parsedRes.type === 'ERROR') {
        actions.errorReceived(data);
        return;
    }
    actions.messageReceived(data.messages);
};

export const subscribe = (actions, userId, stompClient) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                config.ws.messages + userId,
                responseHandler.bind(this, actions)
            );
            stompClient.send(config.ws.history + userId);
            return { stompClient, subscription };
        } catch (error) {
            return connectError;
        }
    } else {
        return connectError;
    }
};

/* eslint-disable no-undef */
export const reconnect = (actions, userId) => {
    return new Promise((resolve, reject) => {
        connect()
            .then(connection => {
                const { stompClient, subscription } = subscribe(
                    actions,
                    userId,
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
        case types.MULTIPLE_SELECT:
        case types.SINGLE_SELECT:
            updated.body.choices = [...message[type]];
            break;
        case types.DATE:
            updated.body.date = message[type];
            break;
        case types.AUDIO:
        case types.VIDEO:
        case types.PHOTO:
            updated.body.URL = message[type];
            break;
        case types.HERO:
            updated.body.imageUri = message[type];
            break;
        default:
            break;
    }

    return JSON.stringify(updated);
};
