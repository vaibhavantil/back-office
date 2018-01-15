import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';
export const setupSocket = (actions, client) => {
    const socket = new SockJS(config.ws.endpoint);
    const stompClient = Stomp.over(socket);

    stompClient.connect(
        {},
        () => {
            stompClient.subscribe(config.ws.messages + client, response => {
                const data = JSON.parse(response.body);
                actions.messageReceived(data);
            });

            stompClient.send(config.ws.history + client);

            return stompClient;
        },
        error => {
            // eslint-disable-next-line
            console.error(error);
            return null;
        }
    );
};
