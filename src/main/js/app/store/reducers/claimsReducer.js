import initialState from '../initialState';
import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIM_TYPES,
    CLAIM_TYPES_SUCCESS,
    CLAIMS_BY_MEMBER,
    CLAIMS_BY_MEMBER_SUCCESS,
    CLAIMS_ERROR
} from '../constants/claims';

export default function(state = initialState.claims, action) {
    switch (action.type) {
        case CLAIM_TYPES:
        case CLAIMS_REQUESTING:
        case CLAIMS_BY_MEMBER:
            return {
                ...state,
                requesting: true
            };

        case CLAIMS_REQUEST_SUCCESS:
            return {
                ...state,
                list: action.claims,
                requesting: false
            };

        case CLAIM_TYPES_SUCCESS:
            return {
                ...state,
                types: action.types.map((type, id) => ({
                    ...type,
                    key: id,
                    value: type.name,
                    text: type.title
                }))
            };

        case CLAIMS_BY_MEMBER_SUCCESS:
            return {
                ...state,
                memberClaims: action.claims,
                requesting: false
            };

        case CLAIMS_ERROR:
            return {
                ...state,
                requesting: false
            };
        default:
            return state;
    }
}
