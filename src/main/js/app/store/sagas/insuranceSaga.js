import { call, put, takeLatest } from 'redux-saga/effects';
import {
    INSURANCE_REQUESTING,
    SAVE_INSURANCE_DATE,
    SEND_CANCEL_REQUEST,
    SEND_CERTIFICATE,
    MEMBER_COMPANY_STATUS
} from 'constants/members';
import api from 'app/api';
import config from 'app/api/config';
import {
    insuranceGetSuccess,
    insuranceGetError,
    saveActivationDateSuccess,
    saveCancellationDateSuccess,
    sendCancelRequestSuccess,
    sendCertificateSuccess,
    changeCompanyStatusSuccess,
    insuranceError
} from '../actions/insuranceActions';
import { showNotification } from '../actions/notificationsActions';
import { DATE, ACTIVATION_DATE, CANCELLATION_DATE } from 'app/lib/messageTypes';

function* requestFlow({ id }) {
    try {
        const { data } = yield call(api, config.insurance.get, null, id);
        yield put(insuranceGetSuccess(data));
    } catch (error) {
        yield [put(insuranceGetError(error.message))];
    }
}

function* saveDateFlow({ id, date, changeType }) {
    try {
        switch (changeType) {
          case ACTIVATION_DATE:
            const activationPath = `${id}/activate`;
            yield call(api, config.insurance.setDate, { activationDate: date }, activationPath);
            yield put(saveActivationDateSuccess(date));
            break;
          case CANCELLATION_DATE:
            const cancellationPath = `${id}/cancel`;
            yield call(api, config.insurance.setDate, { cancellationDate: date }, cancellationPath);
            yield put(saveCancellationDateSuccess(date));
            break;
          default:
            throw "Function: saveDateFlow ErrorMessage: Not valid changeType";
        }
    } catch (error) {
        yield [
            put(
                showNotification({
                    message: error.message,
                    header: 'Insurance'
                })
            ),
            put(insuranceError())
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
        yield [
            put(
                showNotification({
                    message: error.message,
                    header: 'Insurance'
                })
            ),
            put(insuranceError())
        ];
    }
}

function* sendCertificateFlow({ data, memberId }) {
    try {
        const path = `${memberId}/certificate`;
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
            put(insuranceError())
        ];
    }
}

function* changeCompanyStatusFlow({ value, memberId }) {
    try {
        const path = `${memberId}/insuredAtOtherCompany`;
        yield call(
            api,
            config.insurance.companyStatus,
            { insuredAtOtherCompany: value },
            path
        );
        yield [
            put(changeCompanyStatusSuccess(value)),
            put(
                showNotification({
                    message: 'Success',
                    header: '"Insured at other company" field changed',
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
            put(insuranceError())
        ];
    }
}

function* insuranceWatcher() {
    yield [
        takeLatest(INSURANCE_REQUESTING, requestFlow),
        takeLatest(SAVE_INSURANCE_DATE, saveDateFlow),
        takeLatest(SEND_CANCEL_REQUEST, cancelRequestFlow),
        takeLatest(SEND_CERTIFICATE, sendCertificateFlow),
        takeLatest(MEMBER_COMPANY_STATUS, changeCompanyStatusFlow)
    ];
}

export default insuranceWatcher;
