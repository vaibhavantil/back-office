import { call, put, takeLatest, take } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { ASSET_UPDATING, ASSET_REQUESTING } from 'constants/assets';
import { CLIENT_UNSET } from 'constants/login';
import {
    assetUpdateSuccess,
    assetRequestSuccess,
    assetRequestError
} from '../actions/assetsActions';
import { logout } from './loginSaga';

function* assetUpdateFlow({ assetId, assetState }) {
    try {
        const token = yield call(getAuthToken);
        const { data } = yield call(
            api,
            token,
            config.asset.update,
            { state: assetState },
            assetId
        );
        yield put(assetUpdateSuccess(data));
    } catch (error) {
        yield put(assetRequestError(error));
    }
}

function* assetRequestFlow() {
    try {
        const token = yield call(getAuthToken);
        const { data } = yield call(api, token, config.asset.get);
        yield put(assetRequestSuccess(data));
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
