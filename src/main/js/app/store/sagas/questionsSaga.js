import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from '../../api/config';
import { QUESTIONS_REQUESTING, QUESTION_ANSWERING } from 'constants/questions';
import {
    questionsReqSuccess,
    questionsReqError,
    answerSuccess,
    answerError
} from '../actions/questionsActions';
import { showNotification } from '../actions/notificationsActions';

function* requestFlow() {
    try {
        const answered = yield call(api, config.questions.answered);
        const notAnswered = yield call(api, config.questions.notAnsewred);
        yield put(
            questionsReqSuccess({
                answered: answered.data,
                notAnswered: notAnswered.data
            })
        );
    } catch (error) {
        yield [
            put(showNotification({ message: error.message, header: 'Questions' })),
            put(questionsReqError(error))
        ];
    }
}

function* sendAnswerFlow({ data }) {
    try {
        yield call(api, config.questions.sendAnswer, data, data.userId);
        yield put(answerSuccess(data));
    } catch (error) {
        yield [
            put(showNotification({ message: error.message, header: 'Questions' })),
            put(answerError({ message: error.message, hid: data.userId }))
        ];
    }
}

function* watcher() {
    yield [
        takeLatest(QUESTIONS_REQUESTING, requestFlow),
        takeLatest(QUESTION_ANSWERING, sendAnswerFlow)
    ];
}

export default watcher;
