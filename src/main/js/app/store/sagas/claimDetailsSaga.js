import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import { CLAIM_REQUESTING, CLAIM_DETAILS_UPDATING } from '../constants/claims';
import * as actions from '../actions/claimDetailsActions';

function* requestFlow({ id }) {
    try {
        const token = yield call(getAuthToken);
        const claim = yield call(api, token, config.claims.getList, null, id);
        yield put(actions.claimRequestSuccess(claim));
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

function* detailsUpdateFlow({ id, data }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${id}/details`;
        yield call(api, token, config.claims.update, data, path);
        yield put(actions.claimDetailsUpdateSuccess(null));
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

function* watcher() {
    yield [
        takeLatest(CLAIM_REQUESTING, requestFlow),
        takeLatest(CLAIM_DETAILS_UPDATING, detailsUpdateFlow)
    ];
}

export default watcher;
