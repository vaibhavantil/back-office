import {
    PAYMENTS_REQUESTING,
    PAYMENTS_REQUEST_SUCCESS,
    REMOVE_PAYMENT_REQUESTING,
    REMOVE_PAYMENT_SUCCESS,
    CREATE_PAYMENT_REQUESTING,
    CREATE_PAYMENT_SUCCESS,
    UPDATE_RESUME_REQUESTING,
    UPDATE_RESUME_SUCCESS,
    UPDATE_PAYMENT_REQUESTING,
    UPDATE_PAYMENT_SUCCESS
} from '../constants/claims';

export const paymentsRequest = id => ({
    type: PAYMENTS_REQUESTING,
    id
});

export const paymentsRequestSuccess = payments => ({
    type: PAYMENTS_REQUEST_SUCCESS,
    payments
});

export const removePayment = (claimId, paymentId) => ({
    type: REMOVE_PAYMENT_REQUESTING,
    claimId,
    paymentId
});

export const removePaymentSuccess = paymentId => ({
    type: REMOVE_PAYMENT_SUCCESS,
    paymentId
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

export const updatePayment = (id, data) => ({
    type: UPDATE_PAYMENT_REQUESTING,
    data,
    id
});

export const updatePaymentSuccess = payment => ({
    type: UPDATE_PAYMENT_SUCCESS,
    payment
});
