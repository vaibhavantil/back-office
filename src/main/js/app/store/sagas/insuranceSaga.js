import { call, put, takeLatest } from 'redux-saga/effects';
import { INSURANCE_REQUESTING } from 'constants/chatUsers';
import api from 'app/api';
import config from 'app/api/config';
import {
    insuranceGetSuccess,
    insuranceGetError
} from '../actions/insuranceActions';

function* requestFlow({ userId }) {
    try {
        const { data } = yield call(api, config.insurance.get, null, userId);
        yield put(insuranceGetSuccess(data));
    } catch (error) {
        yield put(insuranceGetError(error));
    }
}

function* insuranceWatcher() {
    yield [takeLatest(INSURANCE_REQUESTING, requestFlow)];
}

export default insuranceWatcher;
