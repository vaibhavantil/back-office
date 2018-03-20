import initialState from '../initialState';
import {
    INSURANCE_REQUESTING,
    INSURANCE_REQUEST_SUCCESS,
    INSURANCE_REQUEST_ERROR
} from 'constants/chatUsers';

export default function(state = initialState.insurance, action) {
    switch (action.type) {
        case INSURANCE_REQUESTING:
            return {
                ...state,
                requesting: true,
                successful: false
            };

        case INSURANCE_REQUEST_SUCCESS:
            return {
                ...state,
                requesting: false,
                successful: true,
                data: action.insurance
            };

        case INSURANCE_REQUEST_ERROR:
            return {
                ...state,
                requesting: false,
                error: [...state.error, action.error]
            };

        default:
            return state;
    }
}
