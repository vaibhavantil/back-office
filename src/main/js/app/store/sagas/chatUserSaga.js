import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api/ChatApi';
import { CHATS_REQUESTING, CHAT_SEARCH_REQUESTING } from 'constants';
import {
    chatsRequestSuccess,
    chatsRequestError,
    searchRequestSuccess,
    searchRequestError
} from '../actions/chatUserActions';

function* chatRequestFlow({ client }) {
    try {
        const chats = yield call(api.get, client);
        yield put(chatsRequestSuccess(chats.data));
    } catch (error) {
        yield put(chatsRequestError(error));
    }
}

function* chatSearchRequestFlow({ client, queryString }) {
    try {
        const searchResult = yield call(api.search, client, queryString);
        yield put(searchRequestSuccess(searchResult));
    } catch (error) {
        yield put(searchRequestError(error));
    }
}

function* chatWatcher() {
    yield [
        takeLatest(CHATS_REQUESTING, chatRequestFlow),
        takeLatest(CHAT_SEARCH_REQUESTING, chatSearchRequestFlow)
    ];
}

export default chatWatcher;
