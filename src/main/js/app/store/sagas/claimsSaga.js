import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    CLAIMS_REQUESTING,
    CLAIM_UPDATING,
    CLAIM_CREATING
} from 'constants/claims';
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

function* updateFlow({ data, id }) {
    try {
        const token = yield call(getAuthToken);
        const updatedClaim = yield call(
            api,
            token,
            config.claims.update,
            data,
            id
        );
        yield put(actions.claimsUpdateSuccess(updatedClaim));
    } catch (error) {
        yield put(actions.claimsUpdateError(error));
    }
}

function* createFlow({ data }) {
    try {
        const token = yield call(getAuthToken);
        const newClaim = yield call(api, token, config.claims.create, data);
        yield put(actions.claimCreateSuccess(newClaim));
    } catch (error) {
        yield put(actions.claimCreateError(error));
    }
}

function* claimsWatcher() {
    yield [
        takeLatest(CLAIMS_REQUESTING, requestFlow),
        takeLatest(CLAIM_UPDATING, updateFlow),
        takeLatest(CLAIM_CREATING, createFlow)
    ];
}

export default claimsWatcher;
