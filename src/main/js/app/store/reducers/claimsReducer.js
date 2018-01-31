import initialState from '../initialState';
import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIMS_REQUEST_ERROR
} from 'constants/claims';

export default function(state = initialState.claims, action) {
    switch (action.type) {
        case CLAIMS_REQUESTING:
            return {
                list: state.list,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIMS_REQUEST_SUCCESS:
            return {
                list: action.claims.data,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIMS_REQUEST_ERROR:
            return {
                requesting: false,
                successful: false,
                list: [],
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
