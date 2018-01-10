import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api/ChatApi';
import { CHATS_REQUESTING } from 'constants';
import {
    chatsRequestSuccess,
    chatsRequestError,
} from '../actions/chatUserActions';

function* chatRequestFlow() {
    try {
        const chats = yield call(api.get);
        yield put(chatsRequestSuccess(chats));
    } catch (error) {
        yield put(chatsRequestError(error));
    }
}

function* chatWatcher() {
    yield [takeLatest(CHATS_REQUESTING, chatRequestFlow)];
}

export default chatWatcher;
