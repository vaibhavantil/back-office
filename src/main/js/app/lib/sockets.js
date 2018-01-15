import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';

let stompClient;

export const connect = () => {
    const socket = new SockJS(config.ws.endpoint + '?token=' + JSON.parse(localStorage.getItem('token')));
    stompClient = Stomp.over(socket);
    stompClient.connect(
        {},
        connection => {
            // eslint-disable-next-line
            console.info('connected ', connection);
        },
        error => {
            // eslint-disable-next-line
            console.error(error);
        }
    );
};

export const subscribe = (actions, client) => {
    if (stompClient) {
        const subscription = stompClient.subscribe(
            config.ws.messages + client,
            response => {
                const data = JSON.parse(response.body);
                actions.messageReceived(data);
            }
        );

        stompClient.send(config.ws.history + client);

        return { stompClient, subscription };
    } else {
        return null;
    }
};

export const unsubscribe = subscription => {
    if (subscription) subscription.unsubscribe();
};
