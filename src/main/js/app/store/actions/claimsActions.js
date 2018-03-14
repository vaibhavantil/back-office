import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIMS_REQUEST_ERROR,
    CLAIM_UPDATE_SUCCESS,
    CLAIM_TYPES,
    CLAIM_TYPES_SUCCESS,
    CLAIMS_BY_USER,
    CLAIMS_BY_USER_SUCCESS
} from 'constants/claims';

export const claimsRequest = () => ({
    type: CLAIMS_REQUESTING
});

export const claimsRequestSuccess = claims => ({
    type: CLAIMS_REQUEST_SUCCESS,
    claims
});
export const claimsRequestError = error => ({
    type: CLAIMS_REQUEST_ERROR,
    error
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
