import initialState from '../initialState';
import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR
} from 'constants/claims';

export default function(state = initialState.claimDetails, action) {
    switch (action.type) {
        case CLAIM_REQUESTING:
            return {
                data: state.data,
                requesting: true,
                successful: false,
                errors: []
            };

        case CLAIM_REQUEST_SUCCESS:
            return {
                data: action.claim.data,
                requesting: false,
                successful: true,
                errors: []
            };

        case CLAIM_REQUEST_ERROR:
            return {
                requesting: false,
                successful: false,
                data: state.data,
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
