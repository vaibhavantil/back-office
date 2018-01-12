import {
    CHATS_REQUESTING,
    CHATS_REQUEST_SUCCESS,
    CHATS_REQUEST_ERROR,
    CHAT_SEARCH_REQUESTING,
    CHAT_SEARCH_SUCCESS,
    CHAT_SEARCH_ERROR
} from 'constants';

export const chatsRequest = client => ({
    type: CHATS_REQUESTING,
    client
});

export const chatsRequestSuccess = chats => ({
    type: CHATS_REQUEST_SUCCESS,
    chats
});

export const chatsRequestError = error => ({
    type: CHATS_REQUEST_ERROR,
    error
});

export const searchChatRequest = (client, queryString) => ({
    type: CHAT_SEARCH_REQUESTING,
    client,
    queryString
});

export const searchRequestSuccess = chats => ({
    type: CHAT_SEARCH_SUCCESS,
    chats
});

export const searchRequestError = error => ({
    type: CHAT_SEARCH_ERROR,
    error
});
