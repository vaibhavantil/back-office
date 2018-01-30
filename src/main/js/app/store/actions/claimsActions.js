import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR
} from 'constants';

export const claimsRequest = token => ({
    type: CLAIM_REQUESTING,
    token
});

export const claimsRequestSuccess = claims => ({
    type: CLAIM_REQUEST_SUCCESS,
    claims
});
export const claimsRequestError = error => ({
    type: CLAIM_REQUEST_ERROR,
    error
});
