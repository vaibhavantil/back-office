import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    CREATE_PAYMENT_REQUESTING,
    UPDATE_RESUME_REQUESTING
} from 'constants/claims';
import * as actions from '../actions/paymentActions';
import { claimRequestError } from '../actions/claimDetailsActions';

function* createFlow({ id, data }) {
    try {
        const requestBody = { ...data };
        delete requestBody.date;
        const created = yield call(
            api,
            config.claims.details.create,
            { ...requestBody },
            `${id}/payments`
        );
        yield put(actions.createPaymentSuccess(created.data || data));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* updateResumeFlow({ id, data }) {
    try {
        const path = `${id}/reserve`;
        yield call(api, config.claims.update, { ...data }, path);
        yield put(actions.updateResumeSuccess(data.resume));
    } catch (error) {
        yield put(claimRequestError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(CREATE_PAYMENT_REQUESTING, createFlow),
        takeLatest(UPDATE_RESUME_REQUESTING, updateResumeFlow)
    ];
}

export default watcher;
