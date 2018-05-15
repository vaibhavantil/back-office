import initialState from '../initialState';
import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_ACTIVATION_DATE_SUCCESS,
    SAVE_CANCELLATION_DATE_SUCCESS,
    SEND_CANCEL_REQUEST,
    SEND_CANCEL_REQUEST_SUCCESS,
    SEND_CERTIFICATE,
    SEND_CERTIFICATE_SUCCESS,
    SEND_CERTIFICATE_ERROR,
    MEMBER_COMPANY_STATUS,
    MEMBER_COMPANY_STATUS_SUCCESS
} from '../constants/members';

export default function(state = initialState.insurance, action) {
    switch (action.type) {
        case SEND_CANCEL_REQUEST:
        case INSURANCE_REQUESTING:
        case SAVE_INSURANCE_DATE:
        case SEND_CERTIFICATE:
        case MEMBER_COMPANY_STATUS:
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

        case SAVE_ACTIVATION_DATE_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, insuranceActiveFrom: action.activationDate }
            };

        case SAVE_CANCELLATION_DATE_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, insuranceActiveTo: action.cancellationDate }
            };

        case SEND_CANCEL_REQUEST_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, cancellationEmailSent: true }
            };

        case SEND_CERTIFICATE_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, certificateUploaded: true }
            };

        case MEMBER_COMPANY_STATUS_SUCCESS:
            return {
                ...state,
                requesting: false,
                data: { ...state.data, insuredAtOtherCompany: action.value }
            };

        case SEND_CERTIFICATE_ERROR:
            return {
                ...state,
                requesting: false
            };

        default:
            return state;
    }
}
