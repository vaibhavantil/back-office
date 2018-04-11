import initialState from '../initialState';
import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_ERROR,
    SAVE_INSURANCE_DATE,
    SAVE_DATE_SUCCESS
} from '../constants/members';

export default function(state = initialState.insurance, action) {
    switch (action.type) {
        case INSURANCE_REQUESTING:
        case SAVE_INSURANCE_DATE:
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

        default:
            return state;
    }
}
