import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_DATE_SUCCESS
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
