import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_REQUEST_ERROR
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
