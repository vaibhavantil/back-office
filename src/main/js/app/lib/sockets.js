import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';

let stompClient;

/* eslint-disable no-undef */
export const connect = () => {
    const token = JSON.parse(localStorage.getItem('token'));
    return new Promise((resolve, reject) => {
        //eslint-disable-next-line
        console.log(`${config.ws.endpoint}?token=${token}`);
        const socket = new SockJS(`${config.ws.endpoint}?token=${token}`);
        stompClient = Stomp.over(socket);
        stompClient.connect(
            {},
            () => {
                resolve(stompClient);
            },
            error => {
                reject(error);
            }
        );
    });
};

export const subscribe = (actions, client) => {
    if (stompClient) {
        try {
            const subscription = stompClient.subscribe(
                config.ws.messages + client,
                response => {
                    const data = JSON.parse(response.body);
                    actions.messageReceived(data);
                }
            );

            stompClient.send(config.ws.history + client);

            return { stompClient, subscription };
        } catch (e) {
            return { stompClient: null, subscription: null };
        }
    } else {
        return { stompClient: null, subscription: null };
    }
};

export const unsubscribe = subscription => {
    if (subscription) subscription.unsubscribe();
};

export const reconnect = (actions, client) => {
    connect()
        .then(() => {
            const { stompClient, subscription } = subscribe(actions, client);
            return { stompClient, subscription };
        })
        .catch(() => {
            return { stompClient: null, subscription: null };
        });
};
