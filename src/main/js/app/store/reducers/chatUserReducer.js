import initialState from '../initialState';
import {
    USERS_REQUESTING,
    USERS_REQUEST_SUCCESS,
    USERS_REQUEST_ERROR,
    USER_SEARCH_REQUESTING,
    USER_SEARCH_SUCCESS,
    USER_SEARCH_ERROR
} from 'constants';

export default function(state = initialState.users, action) {
    switch (action.type) {
        case USERS_REQUESTING:
            return {
                ...state,
                requesting: false,
                successful: true,
                errors: []
            };

        case USERS_REQUEST_SUCCESS:
            return {
                list: action.users,
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
        case USER_SEARCH_REQUESTING:
            return {
                ...state,
                requesting: false,
                successful: true,
                errors: []
            };

        case USER_SEARCH_SUCCESS:
            return {
                list: action.users,
                requesting: false,
                successful: true,
                errors: []
            };

        case USER_SEARCH_ERROR:
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
        default:
            return state;
    }
}
