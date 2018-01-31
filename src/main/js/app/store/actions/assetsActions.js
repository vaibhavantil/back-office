import {
    ASSET_UPDATING,
    ASSET_UPDATE_SUCCESS,
    ASSET_UPDATE_ERROR,
    ASSET_REQUESTING,
    ASSET_REQUEST_SUCCESS,
    ASSET_REQUEST_ERROR
} from 'constants';

export const assetUpdate = (assetId, assetState, token) => ({
    type: ASSET_UPDATING,
    assetId,
    assetState,
    token
});

export const assetUpdateSuccess = asset => ({
    type: ASSET_UPDATE_SUCCESS,
    asset
});

export const assetUpdateError = error => ({
    type: ASSET_UPDATE_ERROR,
    error
});

export const assetRequest = token => ({
    type: ASSET_REQUESTING,
    token
});

export const assetRequestSuccess = assets => ({
    type: ASSET_REQUEST_SUCCESS,
    assets
});
export const assetRequestError = error => ({
    type: ASSET_REQUEST_ERROR,
    error
});
