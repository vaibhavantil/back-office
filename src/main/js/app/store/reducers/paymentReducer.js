import initialState from '../initialState';

import {
    PAYMENTS_REQUEST_SUCCESS,
    REMOVE_PAYMENT_SUCCESS,
    UPDATE_RESUME_SUCCESS,
    CREATE_PAYMENT_SUCCESS
} from 'constants/claims';

export default function(state = initialState.claimDetails, action) {
    switch (action.type) {
        case PAYMENTS_REQUEST_SUCCESS:
            return {
                ...state,
                payments: action.payments
            };
        case REMOVE_PAYMENT_SUCCESS:
            return {
                ...state,
                payments: state.payments.filter(
                    el => el.id !== action.paymentId
                )
            };
        case CREATE_PAYMENT_SUCCESS:
            return {
                ...state,
                payments: state.payments.slice().concat(action.payment)
            };

        case UPDATE_RESUME_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    resume: action.resume
                }
            };
        default:
            return state;
    }
}
