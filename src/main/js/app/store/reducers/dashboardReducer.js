import initialState from '../initialState';
import { DASHBOARD_ERROR_RECEIVED, DASHBOARD_UPDATED } from 'constants';

export default function(state = initialState.dashboard, action) {
    switch (action.type) {
        case DASHBOARD_UPDATED:
            return {
                data: action.status,
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
