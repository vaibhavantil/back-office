import { ADD_MESSAGE, MESSAGE_RECEIVED, CLEAR_MESSAGES_LIST } from 'constants';

export const addMessage = (message, messageType, userId, socket) => ({
    type: ADD_MESSAGE,
    message,
    messageType,
    userId,
    socket
});

export const messageReceived = message => ({
    type: MESSAGE_RECEIVED,
    message
});

export const clearMessagesList = () => ({
    type: CLEAR_MESSAGES_LIST
});
