import initialState from '../initialState';
import { setNewMessagesCounter, filterUsersList } from 'app/lib/helpers';
import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_REQUEST_ERROR,
    USER_SEARCH_REQUESTING,
    NEW_MESSAGES_RECEIVED,
    SET_USER_FILTER,
    USERS_SEARCH_SUCCESS
} from 'constants/chatUsers';

export default function(state = initialState.users, action) {
    switch (action.type) {
        case USER_SEARCH_REQUESTING:
        case USERS_REQUESTING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };

        case USERS_REQUEST_SUCCESS:
        case USERS_SEARCH_SUCCESS:
            return {
                ...state,
                list: filterUsersList(action),
                requesting: false,
                successful: true,
                errors: []
            };

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
                filter: action.query.status,
                requesting: true,
                successful: false,
                errors: []
            };

        default:
            return state;
    }
}
