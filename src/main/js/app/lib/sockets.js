import SockJS from 'sockjs-client';
import Stomp from '@stomp/stompjs';
import { ADD_MESSAGE, GET_MESSAGES } from '../store/constants/actionTypes';

export const setupSocket = (actions, client) => {
    // eslint-disable-next-line no-undef
    const socket = new SockJS('/chat');
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        stompClient.send(
            '/app/chat/' + client,
            {},
            JSON.stringify({ type: GET_MESSAGES })
        );

        stompClient.subscribe('/topic/messages/' + client, response => {
            const data = JSON.parse(response);
            switch (data.type) {
                case ADD_MESSAGE:
                    actions.messageReceived(
                        data.message,
                        data.author
                    );
                    break;
                case GET_MESSAGES:
                    actions.messageReceived(
                        data.message,
                        data.author
                    );
                    break;
                default:
                    break;
            }
        });
    });

    return stompClient;
};

/*
socket.onopen = () => {
        socket.send(
            JSON.stringify({
                type: GET_MESSAGES,
                client: client
            })
        );
    };

    socket.onmessage = (event) => {
        const data = JSON.parse(event.data)
        switch (data.type) {
            case ADD_MESSAGE:
                dispatch(messageReceived(data.message, data.author))
                break
            case GET_MESSAGES:
                dispatch(data.users)
                break
            default:
                break
        }
    }*/
