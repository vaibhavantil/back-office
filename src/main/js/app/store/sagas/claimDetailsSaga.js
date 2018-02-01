import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    CLAIM_REQUESTING,
    CREATE_PAYMENT_REQUESTING,
    REMOVE_PAYMENT_REQUESTING,
    CREATE_NOTE_REQUESTING
} from 'constants/claims';
import * as actions from '../actions/claimDetailsActions';

function* requestFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        //eslint-disable-next-line
        console.log(id)
        const claim = yield call(api, token, config.claims.getList, null, id);
        yield put(actions.claimRequestSuccess(claim));
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

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
        takeLatest(CLAIM_REQUESTING, requestFlow),
        takeLatest(CREATE_PAYMENT_REQUESTING, createPaymentFlow),
        takeLatest(REMOVE_PAYMENT_REQUESTING, removePaymentFlow),
        takeLatest(CREATE_NOTE_REQUESTING, createNoteFlow)
    ];
}

export default watcher;
