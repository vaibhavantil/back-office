import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_REQUEST_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_DATE_SUCCESS
} from 'constants/chatUsers';

export const insuranceRequest = userId => ({
    type: INSURANCE_REQUESTING,
    userId
});

export const insuranceGetSuccess = insurance => ({
    type: INSURANCE_REQUEST_SUCCESS,
    insurance
});

export const insuranceGetError = error => ({
    type: INSURANCE_REQUEST_ERROR,
    error
});

export const saveInsuranceDate = (activationDate, userId) => ({
    type: SAVE_INSURANCE_DATE,
    activationDate,
    userId
});

export const saveDateSuccess = date => ({
    type: SAVE_DATE_SUCCESS,
    date
});
