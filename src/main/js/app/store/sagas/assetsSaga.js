import { call, put, takeLatest, select, take } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { ASSET_UPDATING, ASSET_REQUESTING, CLIENT_UNSET } from 'constants';
import {
    assetUpdateSuccess,
    assetUpdateError,
    assetRequestSuccess,
    assetRequestError
} from '../actions/assetsActions';
import { logout } from './loginSaga';

function* assetUpdateFlow({ assetId, assetState, token }) {
    try {
        const { data } = yield call(
            api,
            token,
            config.asset.update,
            { state: assetState },
            assetId
        );
        const { assets: { list } } = yield select();
        const updatedList = list.map(asset => {
            if (asset.id === data.id) {
                return { ...asset, state: data.state };
            }
            return asset;
        });
        yield put(assetUpdateSuccess(updatedList));
    } catch (error) {
        yield put(assetUpdateError(error));
    }
}

function* assetRequestFlow({ token }) {
    try {
        const assets = yield call(api, token, config.asset.get);
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
