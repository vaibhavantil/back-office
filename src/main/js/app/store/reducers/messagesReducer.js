import initialState from '../initialState';
import { MESSAGE_RECEIVED, CLEAR_MESSAGES_LIST } from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case MESSAGE_RECEIVED:
            return {
                list: [...state.list, action.message]
            };
        case CLEAR_MESSAGES_LIST:
            return initialState.messages;
        default:
            return state;
    }
}
