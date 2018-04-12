import {
    ADD_MESSAGE,
    MESSAGE_RECEIVED,
    CLEAR_MESSAGES_LIST,
    SET_ACTIVE_CONNECTION,
    ERROR_RECEIVED,
    MEMBER_REQUESTING,
    MEMBER_REQUEST_SUCCESS,
    MEMBER_REQUEST_ERROR
} from '../constants/members';

export const addMessage = (message, id, socket) => ({
    type: ADD_MESSAGE,
    message,
    id,
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

export const memberRequest = id => ({
    type: MEMBER_REQUESTING,
    id
});

export const memberRequestSuccess = member => ({
    type: MEMBER_REQUEST_SUCCESS,
    member
});

export const memberRequestError = error => ({
    type: MEMBER_REQUEST_ERROR,
    error
});
