import { ADD_MESSAGE, MESSAGE_RECEIVED, GET_MESSAGES_HISTORY } from 'constants';

export const addMessage = (message, socket) => ({
    type: ADD_MESSAGE,
    message,
    socket
});

export const messageReceived = message => ({
    type: MESSAGE_RECEIVED,
    message
});

export const getMessagesHistory = messages => ({
    type: GET_MESSAGES_HISTORY,
    messages
})