import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api/ChatApi';
import { USERS_REQUESTING, USER_SEARCH_REQUESTING } from 'constants';
import {
    usersRequestSuccess,
    usersRequestError,
    searchRequestSuccess,
    searchRequestError
} from '../actions/chatUserActions';

function* chatRequestFlow({ client }) {
    try {
        const users = yield call(api.get, client);
        yield put(usersRequestSuccess(users.data));
    } catch (error) {
        yield put(usersRequestError(error));
    }
}

function* chatSearchRequestFlow({ client, queryString }) {
    try {
        const searchResult = yield call(api.search, client, queryString);
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
