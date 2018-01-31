import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    CREATE_PAYMENT_REQUESTING,
    REMOVE_PAYMENT_REQUESTING,
    CREATE_NOTE_REQUESTING
} from 'contsants/claims';
import * as actions from '../actions/claimDetailsActions';

function* createPaymentFlow({ data }) {
    try {
        const token = yield call(getAuthToken);
        const newPayment = yield call(api, token, config.payment.create, data);
        yield put(actions.addPaymentSuccess(newPayment));
    } catch (error) {
        actions;
    }
}

function* removePaymentFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        const removed = yield call(api, token, config.payment.remove, null, id);
        yield call(actions.removePaymentSuccess(removed));
    } catch (error) {
        yield call(actions.removePaymentError(error));
    }
}

function* createNoteFlow({ data }) {
    try {
        const token = yield call(getAuthToken);
        const newNote = yield call(api, token, config.notes.create, data);
        yield put(actions.claimCreateSuccess(newNote));
    } catch (error) {
        yield put(actions.claimCreateError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(CREATE_PAYMENT_REQUESTING, createPaymentFlow),
        takeLatest(REMOVE_PAYMENT_REQUESTING, removePaymentFlow),
        takeLatest(CREATE_NOTE_REQUESTING, createNoteFlow)
    ];
}

export default watcher;
