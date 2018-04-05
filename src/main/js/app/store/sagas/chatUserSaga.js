import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    USERS_REQUESTING,
    USER_SEARCH_REQUESTING,
    SET_USER_FILTER
} from 'constants/chatUsers';
import {
    usersRequestSuccess,
    usersRequestError,
    searchUsersSuccess
} from '../actions/chatUserActions';
import { showNotification } from '../actions/notificationsActions';

function* usersRequestFlow() {
    try {
        const users = yield call(api, config.users.get);
        yield put(usersRequestSuccess(users.data));
    } catch (error) {
        yield [
            put(usersRequestError(error)),
            put(showNotification({ message: error.message, header: 'Members' }))
        ];
    }
}

function* usersSearchFlow({ query }) {
    try {
        const queryParams = {
            ...query,
            status: query.status === 'ALL' ? '' : query.status
        };
        const searchResult = yield call(
            api,
            config.users.search,
            null,
            '',
            queryParams
        );
        yield put(searchUsersSuccess(searchResult.data));
    } catch (error) {
        yield [
            put(usersRequestError(error)),
            put(showNotification({ message: error.message, header: 'Members' }))
        ];
    }
}

function* chatWatcher() {
    yield [
        takeLatest(USERS_REQUESTING, usersRequestFlow),
        takeLatest(SET_USER_FILTER, usersSearchFlow),
        takeLatest(USER_SEARCH_REQUESTING, usersSearchFlow)
    ];
}

export default chatWatcher;
