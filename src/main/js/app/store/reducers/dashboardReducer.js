import initialState from '../initialState';
import {
    DASHBOARD_ERROR_RECEIVED,
    DASHBOARD_UPDATED,
    UPDATES_REQUEST_SUCCESS
} from 'constants/dashboard';

export default function(state = initialState.dashboard, action) {
    switch (action.type) {
        case DASHBOARD_UPDATED:
            return {
                data: action.status,
                error: null
            };
        case UPDATES_REQUEST_SUCCESS:
            return {
                data: {...state, ...action.status},
                error: null
            };
        case DASHBOARD_ERROR_RECEIVED:
            return {
                ...state,
                error: action.error
            };

        default:
            return state;
    }
}
