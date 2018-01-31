import initialState from '../initialState';
import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR
} from 'constants/claims';

export default function(state = initialState.claims, action) {
    switch (action.type) {
        case CLAIM_REQUESTING:
            return {
                list: state.list,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIM_REQUEST_SUCCESS:
            return {
                list: action.claims.data,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIM_REQUEST_ERROR:
            return {
                requesting: false,
                successful: false,
                messages: [],
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
