import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { CLAIM_REQUESTING } from 'constants';
import * as actions from '../actions/claimsActions';

function* requestFlow() {
    try {
        const token = yield call(getAuthToken);
        const claims = yield call(api, token, config.claims.getList);
        yield put(actions.claimsRequestSuccess(claims));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* claimsWatcher() {
    yield [takeLatest(CLAIM_REQUESTING, requestFlow)];
}

export default claimsWatcher;
