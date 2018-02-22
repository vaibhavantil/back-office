import initialState from '../initialState';
import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR,
    CREATE_NOTE_REQUESTING,
    CREATE_NOTE_SUCCESS,
    CREATE_PAYMENT_REQUESTING,
    UPDATE_RESUME_SUCCESS,
    CREATE_PAYMENT_SUCCESS,
    CLAIM_DETAILS_UPDATING,
    CLAIM_UPDATE_SUCCESS,
    CLAIM_DETAILS_UPDATE_SUCCESS
} from '../constants/claims';

export default function(state = initialState.claimDetails, action) {
    switch (action.type) {
        case CREATE_PAYMENT_REQUESTING:
        case CREATE_NOTE_REQUESTING:
        case CLAIM_REQUESTING:
        case CLAIM_DETAILS_UPDATING:
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

        case CREATE_NOTE_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    notes: [...state.data.notes, action.note]
                }
            };

        case CREATE_PAYMENT_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    payments: [...state.data.payments, action.payment]
                }
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

        case CLAIM_DETAILS_UPDATE_SUCCESS:
            return {
                ...state,
                data: {
                    ...state.data,
                    data: [...state.data.data, ...action.data]
                }
            };

        default:
            return state;
    }
}
