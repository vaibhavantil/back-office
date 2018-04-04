import {
    ADD_MESSAGE,
    MESSAGE_RECEIVED,
    CLEAR_MESSAGES_LIST,
    SET_ACTIVE_CONNECTION,
    ERROR_RECEIVED,
    USER_REQUESTING,
    USER_REQUEST_SUCCESS,
} from '../constants/chatUsers';

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

export const errorReceived = error => ({
    type: ERROR_RECEIVED,
    error
});

export const clearMessagesList = () => ({
    type: CLEAR_MESSAGES_LIST
});

export const setActiveConnection = connection => ({
    type: SET_ACTIVE_CONNECTION,
    connection
});

export const userRequest = userId => ({
    type: USER_REQUESTING,
    userId
});

export const userRequestSuccess = user => ({
    type: USER_REQUEST_SUCCESS,
    user
});
