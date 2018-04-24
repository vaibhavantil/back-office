import initialState from '../initialState';
import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_DATE_SUCCESS,
    SEND_CANCEL_REQUEST,
    SEND_CANCEL_REQUEST_SUCCESS,
    SEND_CERTIFICATE,
    SEND_CERTIFICATE_SUCCESS
} from '../constants/members';

export default function(state = initialState.insurance, action) {
    switch (action.type) {
        case SEND_CANCEL_REQUEST:
        case INSURANCE_REQUESTING:
        case SAVE_INSURANCE_DATE:
        case SEND_CERTIFICATE:
            return {
                ...state,
                requesting: true,
                error: []
            };

        case INSURANCE_REQUEST_SUCCESS:
            return {
                ...state,
                requesting: false,
                successful: true,
                data: action.insurance,
                error: []
            };

        case INSURANCE_ERROR:
            return {
                ...state,
                requesting: false,
                data: null,
                error: [...state.error, action.error]
            };

        case SAVE_DATE_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, insuranceActiveFrom: action.date }
            };

        case SEND_CANCEL_REQUEST_SUCCESS:
            return {
                ...state,
                requesting: false
            };

        case SEND_CERTIFICATE_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: {...state.data, certificateUploaded: true}
            }
        default:
            return state;
    }
}
