import {
    PAYMENTS_REQUESTING,
    PAYMENTS_REQUEST_SUCCESS,
    CREATE_PAYMENT_REQUESTING,
    CREATE_PAYMENT_SUCCESS,
    UPDATE_RESUME_REQUESTING,
    UPDATE_RESUME_SUCCESS
} from '../constants/claims';

export const paymentsRequest = id => ({
    type: PAYMENTS_REQUESTING,
    id
});

export const paymentsRequestSuccess = payments => ({
    type: PAYMENTS_REQUEST_SUCCESS,
    payments
});

export const createPayment = (id, data) => ({
    type: CREATE_PAYMENT_REQUESTING,
    id,
    data
});

export const createPaymentSuccess = payment => ({
    type: CREATE_PAYMENT_SUCCESS,
    payment
});

export const updateResume = (id, data) => ({
    type: UPDATE_RESUME_REQUESTING,
    id,
    data
});

export const updateResumeSuccess = resume => ({
    type: UPDATE_RESUME_SUCCESS,
    resume
});
