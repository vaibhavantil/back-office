import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api/ChatApi';
import { CHATS_REQUESTING } from 'constants';
import {
    chatRequestSuccess,
    chatRequestError,
} from '../actions/chatUserActions';

function* chatRequestFlow() {
    try {
        const users = yield call(api.get);
        yield put(chatRequestSuccess(users));
    } catch (error) {
        yield put(chatRequestError(error));
    }
}

function* chatWatcher() {
    yield [takeLatest(CHATS_REQUESTING, chatRequestFlow)];
}

export default chatWatcher;
