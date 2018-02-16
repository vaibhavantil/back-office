import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_REQUEST_ERROR,
    USER_SEARCH_REQUESTING,
    NEW_MESSAGES_RECEIVED,
    SET_USER_FILTER,
    USERS_SEARCH_SUCCESS
} from 'constants/chatUsers';

export const usersRequest = client => ({
    type: USERS_REQUESTING,
    client
});

export const usersRequestSuccess = users => ({
    type: USERS_REQUEST_SUCCESS,
    users
});

export const usersRequestError = error => ({
    type: USERS_REQUEST_ERROR,
    error
});

export const searchUserRequest = query => ({
    type: USER_SEARCH_REQUESTING,
    query
});

export const searchUsersSuccess = users => ({
    type: USERS_SEARCH_SUCCESS,
    users
});

export const newMessagesReceived = messagesCounters => ({
    type: NEW_MESSAGES_RECEIVED,
    messagesCounters
});

export const setFilter = query => ({
    type: SET_USER_FILTER,
    query
});
