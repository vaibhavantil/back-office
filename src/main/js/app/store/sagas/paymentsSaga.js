import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    CREATE_PAYMENT_REQUESTING,
    UPDATE_RESUME_REQUESTING
} from 'constants/claims';
import * as actions from '../actions/paymentActions';
import { claimRequestError } from '../actions/claimDetailsActions';
import { showNotification } from '../actions/notificationsActions';

function* createFlow({ data, id, userId }) {
    try {
        const requestBody = { ...data, id, userId };
        delete requestBody.date;
        const created = yield call(
            api,
            config.claims.details.create,
            { ...requestBody },
            `${id}/payments`
        );
        yield put(actions.createPaymentSuccess(created.data || data));
    } catch (error) {
        yield [
            put(
                showNotification({ message: error.message, header: 'Payments' })
            ),
            put(claimRequestError(error))
        ];
    }
}

function* updateResumeFlow({ data, id, userId }) {
    try {
        const path = `${id}/reserve`;
        yield call(api, config.claims.update, { ...data, id, userId }, path);
        yield put(actions.updateResumeSuccess(data.resume));
    } catch (error) {
        yield [
            put(
                showNotification({ message: error.message, header: 'Payments' })
            ),
            put(claimRequestError(error))
        ];
    }
}

function* watcher() {
    yield [
        takeLatest(CREATE_PAYMENT_REQUESTING, createFlow),
        takeLatest(UPDATE_RESUME_REQUESTING, updateResumeFlow)
    ];
}

export default watcher;
