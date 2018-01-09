import initialState from '../initialState';
import {
    ASSET_UPDATING,
    ASSET_UPDATE_SUCCESS,
    ASSET_UPDATE_ERROR,
    ASSET_REQUESTING,
    ASSET_REQUEST_SUCCESS,
    ASSET_REQUEST_ERROR
} from 'constants';

export default function(state = initialState.assets, action) {
    switch (action.type) {
        case ASSET_UPDATING:
            return {
                ...state,
                requesting: true,
                successful: false,
                errors: []
            };

        case ASSET_UPDATE_SUCCESS:
            return {
                list: action.asset,
                requesting: false,
                successful: true,
                errors: []
            };

        case ASSET_UPDATE_ERROR:
            return {
                ...state,
                requesting: false,
                successful: false,
                errors: state.errors.concat([
                    {
                        message: action.error.response.message,
                        status: action.error.response.status
                    }
                ])
            };

        case ASSET_REQUESTING:
            return {
                ...state,
                requesting: false,
                successful: true,
                errors: []
            };

        case ASSET_REQUEST_SUCCESS:
            return {
                list: action.assets.data,
                requesting: false,
                successful: true,
                errors: []
            };

        case ASSET_REQUEST_ERROR:
            return {
                requesting: false,
                successful: false,
                messages: [],
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
