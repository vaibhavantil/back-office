import { call, put, takeLatest, select, take } from 'redux-saga/effects';
import api from 'app/api/AssetApi';
import { ASSET_UPDATING, ASSET_REQUESTING, CLIENT_UNSET } from 'constants';
import {
    assetUpdateSuccess,
    assetUpdateError,
    assetRequestSuccess,
    assetRequestError
} from '../actions/assetsActions';
import { logout } from './loginSaga';

function* assetUpdateFlow(action) {
    try {
        const { assetId, assetState } = action;
        // eslint-disable-next-line no-undef
        const token = localStorage.getItem('token');
        const authToken = token ? JSON.parse(token) : '';
        const { data } = yield call(api.update, assetId, assetState, authToken);
        const { assets: { list } } = yield select();
        const updatedList = list.map(asset => {
            if (asset.id === data.id) {
                return {...asset, state: data.state};
            }
            return asset;
        });
        yield put(assetUpdateSuccess(updatedList));
    } catch (error) {
        yield put(assetUpdateError(error));
    }
}

function* assetRequestFlow(action) {
    try {
        const { client } = action;
        const assets = yield call(api.get, client);
        yield put(assetRequestSuccess(assets));
    } catch (error) {
        yield put(assetRequestError(error));
    }
}

function* assetsWatcher() {
    yield [
        takeLatest(ASSET_UPDATING, assetUpdateFlow),
        takeLatest(ASSET_REQUESTING, assetRequestFlow)
    ];
    const action = yield take(CLIENT_UNSET);
    if (action.type === CLIENT_UNSET) yield call(logout);
}

export default assetsWatcher;
