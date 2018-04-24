import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INSURANCE_REQUESTING,
    SAVE_INSURANCE_DATE,
    SEND_CANCEL_REQUEST,
    SEND_CERTIFICATE
} from 'constants/members';
import api from 'app/api';
import config from 'app/api/config';
import {
    insuranceGetSuccess,
    insuranceGetError,
    saveDateSuccess,
    sendCancelRequestSuccess,
    sendCertificateSuccess
} from '../actions/insuranceActions';
import { showNotification } from '../actions/notificationsActions';

function* requestFlow({ id }) {
    try {
        const { data } = yield call(api, config.insurance.get, null, id);
        yield put(insuranceGetSuccess(data));
    } catch (error) {
        yield [put(insuranceGetError(error.message))];
    }
}

function* saveDateFlow({ id, activationDate }) {
    try {
        const path = `${id}/activate`;
        yield call(api, config.insurance.setDate, { activationDate }, path);
        yield put(saveDateSuccess(activationDate));
    } catch (error) {
        yield [
            put(
                showNotification({
                    message: error.message,
                    header: 'Insurance'
                })
            ),
            put(insuranceGetError(error.message))
        ];
    }
}

function* cancelRequestFlow({ id }) {
    try {
        const path = `${id}/sendCancellationEmail`;
        yield call(api, config.insurance.cancel, null, path);
        yield [
            put(sendCancelRequestSuccess()),
            put(
                showNotification({
                    message: 'Success',
                    header: 'Send cancellation email to existing insurer',
                    type: 'olive'
                })
            )
        ];
    } catch (error) {
        yield [put(insuranceGetError(error.message))];
    }
}

function* sendCertificateFlow({ data, hid }) {
    try {
        const path = `${hid}/certificate`;
        yield call(api, config.insurance.cert, data, path);
        yield [
            put(sendCertificateSuccess()),
            put(
                showNotification({
                    message: 'Success',
                    header: 'Upload insurance certificate',
                    type: 'olive'
                })
            )
        ];
    } catch (error) {
        yield [
            put(
                showNotification({
                    message: error.message,
                    header: 'Insurance'
                })
            ),
            put(sendCancelRequestSuccess())
        ];
    }
}

function* insuranceWatcher() {
    yield [
        takeLatest(INSURANCE_REQUESTING, requestFlow),
        takeLatest(SAVE_INSURANCE_DATE, saveDateFlow),
        takeLatest(SEND_CANCEL_REQUEST, cancelRequestFlow),
        takeLatest(SEND_CERTIFICATE, sendCertificateFlow)
    ];
}

export default insuranceWatcher;
