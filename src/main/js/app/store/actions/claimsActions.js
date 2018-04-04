import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIM_UPDATE_SUCCESS,
    CLAIM_TYPES,
    CLAIM_TYPES_SUCCESS,
    CLAIMS_BY_USER,
    CLAIMS_BY_USER_SUCCESS,
    CLAIMS_ERROR
} from '../constants/claims';

export const claimsRequest = () => ({
    type: CLAIMS_REQUESTING
});

export const claimsRequestSuccess = claims => ({
    type: CLAIMS_REQUEST_SUCCESS,
    claims
});

export const claimUpdateSuccess = (reqType, data) => ({
    type: CLAIM_UPDATE_SUCCESS,
    reqType,
    data
});

export const claimTypes = () => ({
    type: CLAIM_TYPES
});

export const claimsTypesSuccess = types => ({
    type: CLAIM_TYPES_SUCCESS,
    types
});

export const claimsByUser = id => ({
    type: CLAIMS_BY_USER,
    id
});

export const claimsByUserSuccess = claims => ({
    type: CLAIMS_BY_USER_SUCCESS,
    claims
});

export const claimsError = error => ({
    type: CLAIMS_ERROR,
    error
});
