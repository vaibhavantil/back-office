import { call, put, takeLatest } from 'redux-saga/effects';

import { INSURANCE_REQUESTING, SAVE_INSURANCE_DATE } from 'constants/chatUsers';
import api from 'app/api';
import config from 'app/api/config';
import {
    insuranceGetSuccess,
    insuranceGetError,
    saveDateSuccess
} from '../actions/insuranceActions';

function* requestFlow({ userId }) {
    try {
        const { data } = yield call(api, config.insurance.get, null, userId);
        yield put(insuranceGetSuccess(data));
    } catch (error) {
        yield put(insuranceGetError(error));
    }
}

function* saveDateFlow({ userId, date }) {
    try {
        yield call(api, config.insurance.setDate, { date }, userId);
        yield put(saveDateSuccess(date));
    } catch (error) {
        yield put(insuranceGetError(error));
    }
}

function* insuranceWatcher() {
    yield [
        takeLatest(INSURANCE_REQUESTING, requestFlow),
        takeLatest(SAVE_INSURANCE_DATE, saveDateFlow)
    ];
}

export default insuranceWatcher;
