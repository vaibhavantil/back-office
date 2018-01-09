import initialState from '../initialState';
import { ADD_MESSAGE, MESSAGE_RECEIVED } from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case ADD_MESSAGE:
        case MESSAGE_RECEIVED:
            return state.list.concat([
                {
                    message: action.message,
                    author: action.author,
                    id: action.id
                }
            ]);
        default:
            return state;
    }
}
