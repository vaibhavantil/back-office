import {
    CHATS_REQUESTING,
    CHATS_REQUEST_SUCCESS,
    CHATS_REQUEST_ERROR
} from 'constants';

export const chatsRequest = () => ({
    type: CHATS_REQUESTING
});

export const chatsRequestSuccess = chats => ({
    type: CHATS_REQUEST_SUCCESS,
    chats
});

export const chatsRequestError = error => ({
    type: CHATS_REQUEST_ERROR,
    error
});
