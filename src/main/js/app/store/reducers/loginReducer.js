import initialState from '../initialState';
import {
    LOGIN_REQUESTING,
    LOGIN_SUCCESS,
    LOGIN_ERROR
} from 'constants';

export default function(state = initialState.login, action) {
    switch (action.type) {
        case LOGIN_REQUESTING:
            return {
                requesting: true,
                successful: false,
                errors: []
            };

        case LOGIN_SUCCESS:
            return {
                errors: [],
                messages: [],
                requesting: false,
                successful: true
            };

        case LOGIN_ERROR:
            return {
                errors: state.errors.concat([
                    {
                        message: action.error.response.statusText,
                        status: action.error.response.status
                    }
                ]),
                requesting: false,
                successful: false
            };

        default:
            return state;
    }
}
