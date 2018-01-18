import initialState from '../initialState';
import {
    MESSAGE_RECEIVED,
    CLEAR_MESSAGES_LIST,
    SET_ACTIVE_CONNECTION
} from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case MESSAGE_RECEIVED:
            return {
                activeConnection: state.activeConnection,
                list: [...state.list, action.message]
            };
        case CLEAR_MESSAGES_LIST:
            return {
                activeConnection: state.activeConnection,
                list: []
            };
        case SET_ACTIVE_CONNECTION:
            return {
                list: state.list,
                activeConnection: action.connection
            };
        default:
            return state;
    }
}
