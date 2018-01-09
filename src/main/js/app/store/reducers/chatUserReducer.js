import initialState from '../initialState';
import {
    CHATS_REQUESTING,
    CHATS_REQUEST_SUCCESS,
    CHATS_REQUEST_ERROR
} from 'constants';

export default function(state = initialState.messages, action) {
    switch (action.type) {
        case CHATS_REQUESTING:
            return {
                ...state,
                users: {
                    requesting: false,
                    successful: true,
                    errors: []
                }
            };

        case CHATS_REQUEST_SUCCESS:
            return {
                ...state,
                users: {
                    users: action.users.data,
                    requesting: false,
                    successful: true,
                    errors: []
                }
            };

        case CHATS_REQUEST_ERROR:
            return {
                ...state,
                users: {
                    requesting: false,
                    successful: false,
                    messages: [],
                    errors: state.errors.concat([
                        {
                            message: action.error.response.statusText,
                            status: action.error.response.status
                        }
                    ])
                }
            };
        default:
            return state;
    }
}
