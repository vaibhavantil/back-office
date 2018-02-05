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
    CREATE_PAYMENT_REQUESTING
} from '../constants/claims';

export default function(state = initialState.claimDetails, action) {
    switch (action.type) {
        case PAYMENTS_REQUESTING:
        case NOTES_REQUESTING:
        case REMOVE_PAYMENT_REQUESTING:
        case CREATE_PAYMENT_REQUESTING:
        case REMOVE_NOTE_REQUESTING:
        case CREATE_NOTE_REQUESTING:
        case CLAIM_REQUESTING:
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
                errors: state.errors.concat([
                    {
                        message: action.error.response.statusText,
                        status: action.error.response.status
                    }
                ])
            };

        case NOTES_REQUEST_SUCCESS:
            return {
                ...state,
                notes: action.notes.data
            };

        case CREATE_NOTE_SUCCESS:
            return {
                ...state,
                notes: state.notes.slice().concat(action.note)
            };

        case REMOVE_NOTE_SUCCESS:
            return {
                ...state,
                notes: state.notes.filter(el => el.id !== action.noteId)
            };

        default:
            return state;
    }
}