import {
    CLAIMS_REQUESTING,
    CLAIMS_REQUEST_SUCCESS,
    CLAIMS_REQUEST_ERROR,
    CLAIM_UPDATING,
    CLAIM_UPDATE_SUCCESS,
    CLAIM_TYPES,
    CLAIM_TYPES_SUCCESS
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

export const claimUpdate = (id, data, reqType) => ({
    type: CLAIM_UPDATING,
    id,
    data,
    reqType
});

export const claimUpdateSuccess = () => ({
    type: CLAIM_UPDATE_SUCCESS
});

export const claimTypes = () => ({
    type: CLAIM_TYPES
});

export const claimsTypesSuccess = types => ({
    type: CLAIM_TYPES_SUCCESS,
    types
});
