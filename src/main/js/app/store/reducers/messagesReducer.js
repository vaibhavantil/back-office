import initialState from '../initialState';
import {
    MESSAGE_RECEIVED,
    CLEAR_MESSAGES_LIST,
    SET_ACTIVE_CONNECTION,
    ERROR_RECEIVED
} from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case MESSAGE_RECEIVED:
            return {
                ...state,
                list: [...state.list, ...action.message]
            };
        case CLEAR_MESSAGES_LIST:
            return {
                ...state,
                list: [],
                error: null
            };
        case SET_ACTIVE_CONNECTION:
            return {
                ...state,
                activeConnection: action.connection
            };

        case ERROR_RECEIVED:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
}
