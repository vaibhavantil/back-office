import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from '../../lib/checkAuth';
import {
    USERS_REQUESTING,
    USER_SEARCH_REQUESTING,
    SET_USER_FILTER
} from 'constants/chatUsers';
import {
    usersRequestSuccess,
    usersRequestError
} from '../actions/chatUserActions';

function* usersRequestFlow() {
    try {
        const token = getAuthToken();
        const users = yield call(api, token, config.users.get);
        yield put(usersRequestSuccess(users.data));
    } catch (error) {
        yield put(usersRequestError(error));
    }
}

function* usersSearchFlow({ query }) {
    try {
        const token = getAuthToken();
        const queryParams = {
            ...query,
            status: query.status === 'ALL' ? '' : query.status
        };
        const searchResult = yield call(
            api,
            token,
            config.users.search,
            null,
            '',
            queryParams
        );
        yield put(usersRequestSuccess(searchResult.data));
    } catch (error) {
        yield put(usersRequestError(error));
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
