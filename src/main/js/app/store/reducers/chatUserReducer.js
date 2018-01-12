import initialState from '../initialState';
import {
    CHATS_REQUESTING,
    CHATS_REQUEST_SUCCESS,
    CHATS_REQUEST_ERROR
} from 'constants';

export default function(state = initialState.chats, action) {
    switch (action.type) {
        case CHATS_REQUESTING:
            return {
                ...state,
                requesting: false,
                successful: true,
                errors: []
            };

        case CHATS_REQUEST_SUCCESS:
            return {
                list: action.chats,
                requesting: false,
                successful: true,
                errors: []
            };

        case CHATS_REQUEST_ERROR:
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
