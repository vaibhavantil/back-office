import { ADD_MESSAGE, GET_MESSAGES } from '../store/constants/actionTypes';
import { messageReceived } from '../store/actions/messagesActions';

export const setupSocket = (dispatch, client) => {
    // eslint-disable-next-line no-undef
    const socket = new WebSocket('ws://localhost:8989');

    socket.onopen = () => {
        socket.send(
            JSON.stringify({
                type: GET_MESSAGES,
                client: client
            })
        );
    };

    socket.onmessage = event => {
        const data = JSON.parse(event.data);
        switch (data.type) {
            case ADD_MESSAGE:
                dispatch(messageReceived(data.message, data.author));
                break;
            default:
                break;
        }
    };

    return socket;
};
