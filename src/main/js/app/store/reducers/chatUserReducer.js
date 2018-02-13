import initialState from '../initialState';
import { setNewMessagesCounter } from 'app/lib/helpers';
import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_REQUEST_ERROR,
    USER_SEARCH_REQUESTING,
    USER_SEARCH_SUCCESS,
    USER_SEARCH_ERROR,
    NEW_MESSAGES_RECEIVED,
    SET_USER_FILTER
} from 'constants/chatUsers';

export default function(state = initialState.users, action) {
    switch (action.type) {
        case USER_SEARCH_REQUESTING:
        case USERS_REQUESTING:
            return {
                ...state,
                requesting: false,
                successful: true,
                errors: []
            };

        case USER_SEARCH_SUCCESS:
        case USERS_REQUEST_SUCCESS:
            return {
                ...state,
                list: action.users.map((el, id) => ({
                    ...el,
                    status: id % 2 ? 'INACTIVE' : 'ACTIVE'
                })),
                requesting: false,
                successful: true,
                errors: []
            };

        case USER_SEARCH_ERROR:
        case USERS_REQUEST_ERROR:
            return {
                ...state,
                requesting: false,
                successful: false,
                errors: state.errors.concat([
                    {
                        message: action.error.response.statusText,
                        status: action.error.response.status
                    }
                ])
            };

        case NEW_MESSAGES_RECEIVED:
            return {
                ...state,
                list: setNewMessagesCounter(
                    state.list.slice(),
                    action.messagesCouters
                )
            };

        case SET_USER_FILTER:
            return {
                ...state,
                filter: action.filter
            };
        default:
            return state;
    }
}
