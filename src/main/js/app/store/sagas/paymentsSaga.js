import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    PAYMENTS_REQUESTING,
    CREATE_PAYMENT_REQUESTING,
    REMOVE_PAYMENT_REQUESTING,
    UPDATE_RESUME_REQUESTING,
    UPDATE_PAYMENT_REQUESTING
} from 'constants/claims';
import * as actions from '../actions/paymentActions';
import { claimRequestError } from '../actions/claimDetailsActions';

function* requestFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        const list = yield call(
            api,
            token,
            config.claims.details.get,
            null,
            `${id}/payouts`
        );
        yield put(actions.paymentsRequestSuccess(list));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* createFlow({ id, data }) {
    try {
        const token = yield call(getAuthToken);
        const created = yield call(
            api,
            token,
            config.claims.details.create,
            data,
            `${id}/payouts`
        );
        yield put(actions.createPaymentSuccess(created));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* removeFlow({ claimId, paymentId }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${claimId}/payouts/${paymentId}`;
        yield call(api, token, config.claims.details.remove, null, path);
        yield put(actions.removePaymentSuccess(paymentId));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* updateResumeFlow({ id, data }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${id}/resume`;
        yield call(api, token, config.claims.update, data, path);
        yield put(actions.updateResumeSuccess(data.resume));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* updatePaymentFlow({ id, data }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${id}/payout`;
        yield call(api, token, config.claims.update, data, path);
        yield put(actions.updatePaymentSuccess(data));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(PAYMENTS_REQUESTING, requestFlow),
        takeLatest(CREATE_PAYMENT_REQUESTING, createFlow),
        takeLatest(REMOVE_PAYMENT_REQUESTING, removeFlow),
        takeLatest(UPDATE_RESUME_REQUESTING, updateResumeFlow),
        takeLatest(UPDATE_PAYMENT_REQUESTING, updatePaymentFlow)
    ];
}

export default watcher;
