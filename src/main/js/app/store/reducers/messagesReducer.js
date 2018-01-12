import initialState from '../initialState';
import { ADD_MESSAGE, MESSAGE_RECEIVED, GET_MESSAGES_HISTORY } from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case ADD_MESSAGE:
        case MESSAGE_RECEIVED:
            return {
                list: [...state.list, { message: action.message }]
            };
        case GET_MESSAGES_HISTORY:
            return {
                list: [...state.list, ...action.messages]
            };
        default:
            return state;
    }
}
