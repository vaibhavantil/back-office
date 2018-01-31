import {
    CLAIM_REQUESTING,
    CLAIM_REQUEST_SUCCESS,
    CLAIM_REQUEST_ERROR
} from 'constants/claims';

export const claimRequest = id => ({
    type: CLAIM_REQUESTING,
    id
});

export const claimRequestSuccess = claim => ({
    type: CLAIM_REQUEST_SUCCESS,
    claim
});
export const claimRequestError = error => ({
    type: CLAIM_REQUEST_ERROR,
    error
});
