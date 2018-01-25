import initialState from '../initialState';
import { refreshMessagesList } from 'app/lib/helpers';
import {
    MESSAGE_RECEIVED,
    CLEAR_MESSAGES_LIST,
    SET_ACTIVE_CONNECTION,
    ERROR_RECEIVED,
    USER_REQUESTING,
    USER_REQUEST_SUCCESS,
    USER_REQUEST_ERROR
} from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case MESSAGE_RECEIVED:
            return {
                ...state,
                list: refreshMessagesList(state.list.slice(), action.message)
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
        case USER_REQUESTING:
            return {
                ...state
            };

        case USER_REQUEST_SUCCESS:
            return {
                ...state,
                user: action.user
            };

        case USER_REQUEST_ERROR:
            return {
                ...state,
                error: action.error
            };
        default:
            return state;
    }
}
