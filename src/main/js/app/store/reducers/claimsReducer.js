import initialState from '../initialState';
import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIMS_REQUEST_ERROR,
    CLAIM_TYPES,
    CLAIM_TYPES_SUCCESS
} from 'constants/claims';

export default function(state = initialState.claims, action) {
    switch (action.type) {
        case CLAIM_TYPES:
        case CLAIMS_REQUESTING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };

        case CLAIMS_REQUEST_SUCCESS:
            return {
                ...state,
                list: action.claims.data,
                requesting: false,
                successful: true,
                errors: []
            };
        
        case CLAIMS_REQUEST_ERROR:
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

        case CLAIM_TYPES_SUCCESS:
            return {
                ...state,
                types: action.types.data.map((type, id) => ({
                    ...type,
                    key: id,
                    value: type.name,
                    text: type.title
                }))
            };
        default:
            return state;
    }
}
