import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { QUESTIONS_REQUESTING } from 'constants/questions';
import {
    questionsReqSuccess,
    questionsReqError
} from '../actions/questionsActions';

function* requestFlow() {
    try {
        const token = yield call(getAuthToken);
        const { data } = yield call(api, token, config.questions.get);
        yield put(questionsReqSuccess(data));
    } catch (error) {
        yield put(questionsReqError(error));
    }
}

function* watcher() {
    yield [takeLatest(QUESTIONS_REQUESTING, requestFlow)];
}

export default watcher;
