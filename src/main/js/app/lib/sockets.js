import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';

const connectError = { stompClient: null, subscription: null };

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

export const subscribe = (actions, clientId, stompClient) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                config.ws.messages + clientId,
                response => {
                    const data = JSON.parse(response.body);
                    actions.messageReceived(data);
                }
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
