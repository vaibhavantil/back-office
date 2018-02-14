import initialState from '../initialState';
import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR,
    NOTES_REQUESTING,
    NOTES_REQUEST_SUCCESS,
    CREATE_NOTE_REQUESTING,
    CREATE_NOTE_SUCCESS,
    REMOVE_NOTE_REQUESTING,
    REMOVE_NOTE_SUCCESS,
    PAYMENTS_REQUESTING,
    REMOVE_PAYMENT_REQUESTING,
    CREATE_PAYMENT_REQUESTING,
    PAYMENTS_REQUEST_SUCCESS,
    REMOVE_PAYMENT_SUCCESS,
    UPDATE_RESUME_SUCCESS,
    CREATE_PAYMENT_SUCCESS,
    CLAIM_DETAILS_UPDATING,
    UPDATE_PAYMENT_REQUESTING,
    CLAIM_UPDATE_SUCCESS,
    UPDATE_PAYMENT_SUCCESS
} from '../constants/claims';

import { updatePayments } from 'app/lib/helpers';

export default function(state = initialState.claimDetails, action) {
    switch (action.type) {
        case PAYMENTS_REQUESTING:
        case NOTES_REQUESTING:
        case REMOVE_PAYMENT_REQUESTING:
        case CREATE_PAYMENT_REQUESTING:
        case REMOVE_NOTE_REQUESTING:
        case CREATE_NOTE_REQUESTING:
        case CLAIM_REQUESTING:
        case CLAIM_DETAILS_UPDATING:
        case UPDATE_PAYMENT_REQUESTING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };

        case CLAIM_REQUEST_SUCCESS:
            return {
                ...state,
                data: action.claim.data,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIM_REQUEST_ERROR:
            return {
                ...state,
                requesting: false,
                successful: false,
                errors: [
                    ...state.errors,
                    ...{
                        message: action.error.response.statusText,
                        status: action.error.response.status
                    }
                ]
            };

        case NOTES_REQUEST_SUCCESS:
            return {
                ...state,
                notes: action.notes.data
            };

        case CREATE_NOTE_SUCCESS:
            return {
                ...state,
                notes: [...state.notes, action.note]
            };

        case REMOVE_NOTE_SUCCESS:
            return {
                ...state,
                notes: state.notes.filter(el => el.id !== action.noteId)
            };

        case PAYMENTS_REQUEST_SUCCESS:
            return {
                ...state,
                payments: action.payments.data
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
                payments: [...state.payments, action.payment.data]
            };

        case UPDATE_RESUME_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    resume: action.resume
                }
            };

        case CLAIM_UPDATE_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    [action.reqType]: action.data[action.reqType]
                }
            };

        case UPDATE_PAYMENT_SUCCESS: {
            return {
                ...state,
                payments: updatePayments(state.payments, action.payment)
            };
        }

        default:
            return state;
    }
}
