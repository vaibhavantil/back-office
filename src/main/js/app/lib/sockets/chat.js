import config from 'app/api/config';
import { connect } from './index';

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
