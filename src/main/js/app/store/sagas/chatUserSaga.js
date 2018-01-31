import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from '../../lib/checkAuth';
import { USERS_REQUESTING, USER_SEARCH_REQUESTING } from 'constants';
import {
    usersRequestSuccess,
    usersRequestError,
    searchRequestSuccess,
    searchRequestError
} from '../actions/chatUserActions';

function* chatRequestFlow() {
    try {
        const token = getAuthToken();
        const users = yield call(api, token, config.users.get);
        yield put(usersRequestSuccess(users.data));
    } catch (error) {
        yield put(usersRequestError(error));
    }
}

function* chatSearchRequestFlow({ queryString }) {
    try {
        const token = getAuthToken();
        const searchResult = yield call(
            api,
            token,
            config.users.search,
            null,
            '',
            { q: queryString }
        );
        yield put(searchRequestSuccess(searchResult.data));
    } catch (error) {
        yield put(searchRequestError(error));
    }
}

function* chatWatcher() {
    yield [
        takeLatest(USERS_REQUESTING, chatRequestFlow),
        takeLatest(USER_SEARCH_REQUESTING, chatSearchRequestFlow)
    ];
}

export default chatWatcher;
