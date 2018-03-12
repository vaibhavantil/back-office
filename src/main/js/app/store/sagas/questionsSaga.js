import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from '../../api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { QUESTIONS_REQUESTING, QUESTION_ANSWERING } from 'constants/questions';
import {
    questionsReqSuccess,
    questionsReqError,
    answerSuccess
} from '../actions/questionsActions';

function* requestFlow() {
    try {
        const token = yield call(getAuthToken);
        const answered = yield call(api, token, config.questions.answered);
        const notAnswered = yield call(
            api,
            token,
            config.questions.notAnsewred
        );
        yield put(
            questionsReqSuccess({
                answered: answered.data,
                notAnswered: notAnswered.data
            })
        );
    } catch (error) {
        yield put(questionsReqError(error));
    }
}

function* sendAnswerFlow({ data }) {
    try {
        const token = yield call(getAuthToken);
        yield call(api, token, config.questions.sendAnswer, data, data.userId);
        yield put(answerSuccess(data));
    } catch (error) {
        yield put(questionsReqError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(QUESTIONS_REQUESTING, requestFlow),
        takeLatest(QUESTION_ANSWERING, sendAnswerFlow)
    ];
}

export default watcher;
