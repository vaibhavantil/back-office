import initialState from '../initialState';
import { setNewMessagesCounter, filterUsersList } from '../../lib/helpers';
import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_ERROR,
    USER_SEARCH_REQUESTING,
    NEW_MESSAGES_RECEIVED,
    SET_USER_FILTER,
    USERS_SEARCH_SUCCESS
} from '../constants/chatUsers';

export default function(state = initialState.users, action) {
    switch (action.type) {
        case USER_SEARCH_REQUESTING:
        case USERS_REQUESTING:
            return {
                ...state,
                requesting: true
            };

        case USERS_REQUEST_SUCCESS:
        case USERS_SEARCH_SUCCESS:
            return {
                ...state,
                list: filterUsersList(action),
                requesting: false
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
                requesting: true
            };

        case USERS_ERROR:
            return {
                ...state,
                requesting: false
            };

        default:
            return state;
    }
}
