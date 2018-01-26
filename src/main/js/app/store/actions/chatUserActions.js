import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_REQUEST_ERROR,
    USER_SEARCH_REQUESTING,
    USER_SEARCH_SUCCESS,
    USER_SEARCH_ERROR
} from 'constants';

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

export const searchUserRequest = (client, queryString) => ({
    type: USER_SEARCH_REQUESTING,
    client,
    queryString
});

export const searchRequestSuccess = users => ({
    type: USER_SEARCH_SUCCESS,
    users
});

export const searchRequestError = error => ({
    type: USER_SEARCH_ERROR,
    error
});
