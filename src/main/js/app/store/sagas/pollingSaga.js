import { take, put, call, race } from 'redux-saga/effects';
import { delay } from 'redux-saga';
import api from 'app/api';
import config from 'app/api/config';
import { POLL_START, POLL_STOP } from 'constants';
import {
    assetRequestError,
    assetRequestSuccess
} from '../actions/assetsActions';

function* pollAssetsSaga({ client, duration }) {
    while (true) {
        try {
            const assets = yield call(api, client, config.assets.get);
            yield put(assetRequestSuccess(assets));
            yield call(delay, duration);
        } catch (error) {
            yield put(assetRequestError(error));
        }
    }
}

function* pollAssetsSagaWatcher() {
    while (true) {
        const actionInfo = yield take(POLL_START);
        yield race([call(pollAssetsSaga, actionInfo), take(POLL_STOP)]);
    }
}

export default pollAssetsSagaWatcher;
