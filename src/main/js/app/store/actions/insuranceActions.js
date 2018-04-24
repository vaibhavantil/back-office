import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_DATE_SUCCESS,
    SEND_CANCEL_REQUEST,
    SEND_CANCEL_REQUEST_SUCCESS,
    SEND_CERTIFICATE
} from '../constants/members';

export const insuranceRequest = id => ({
    type: INSURANCE_REQUESTING,
    id
});

export const insuranceGetSuccess = insurance => ({
    type: INSURANCE_REQUEST_SUCCESS,
    insurance
});

export const insuranceGetError = error => ({
    type: INSURANCE_ERROR,
    error
});

export const saveInsuranceDate = (activationDate, id) => ({
    type: SAVE_INSURANCE_DATE,
    activationDate,
    id
});

export const saveDateSuccess = date => ({
    type: SAVE_DATE_SUCCESS,
    date
});

export const sendCancelRequest = id => ({
    type: SEND_CANCEL_REQUEST,
    id
});

export const sendCancelRequestSuccess = () => ({
    type: SEND_CANCEL_REQUEST_SUCCESS
});

export const sendCertificate = (data, hid) => ({
    type: SEND_CERTIFICATE,
    data,
    hid
});
