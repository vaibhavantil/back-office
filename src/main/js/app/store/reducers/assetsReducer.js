import initialState from '../initialState';
import {
    ASSET_UPDATING,
    ASSET_UPDATE_SUCCESS,
    ASSET_UPDATE_ERROR,
    ASSET_REQUESTING,
    ASSET_REQUEST_SUCCESS,
    ASSET_REQUEST_ERROR
} from 'constants/assets';

export default function(state = initialState.assets, action) {
    switch (action.type) {
        case ASSET_REQUESTING:
        case ASSET_UPDATING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };

        case ASSET_UPDATE_SUCCESS:
            return {
                list: state.list.map(
                    asset =>
                        asset.id === action.asset.id
                            ? { ...asset, state: action.asset.state }
                            : asset
                ),
                requesting: false,
                successful: true,
                errors: []
            };

        case ASSET_REQUEST_ERROR:
        case ASSET_UPDATE_ERROR:
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

        case ASSET_REQUEST_SUCCESS:
            return {
                // TODO: remove data mock
                list: action.assets.data.map(el => ({
                    ...el,
                    price: 1000,
                    purchaseDate: `${Math.floor(Math.random() * 10 + 1)}/03/2020`
                })),
                requesting: false,
                successful: true,
                errors: []
            };

        default:
            return state;
    }
}
