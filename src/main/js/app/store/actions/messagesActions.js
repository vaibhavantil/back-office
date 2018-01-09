import { ADD_MESSAGE, MESSAGE_RECEIVED } from 'constants';

let msgId = 0;

export const addMessage = (message, user) => ({
    type: ADD_MESSAGE,
    id: msgId++,
    message,
    user
});

export const messageReceived = (message, user) => ({
    type: MESSAGE_RECEIVED,
    id: msgId++,
    message,
    user
});
