import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import config from 'app/api/config';
export const setupSocket = (actions, client) => {
    const socket = new SockJS(config.ws.endpoint);
    const stompClient = Stomp.over(socket);

    stompClient.connect(
        {},
        () => {
            stompClient.send(config.ws.history + client);

            // todo add websocket endpoint 2 get messagesList
            stompClient.subscribe(config.ws.history + client, response => {
                const data = JSON.parse(response);
                actions.getMessagesHistory(data.body);
                stompClient.unsubscribe(config.ws.history + client, () => {});
            });

            stompClient.subscribe(+client, response => {
                const data = JSON.parse(response);
                actions.messageReceived(data.body);
            });
            return stompClient;
        },
        error => {
            // eslint-disable-next-line
            console.error(error);
            return null;
        }
    );
};
