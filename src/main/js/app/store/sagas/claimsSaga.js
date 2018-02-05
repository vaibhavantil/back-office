import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import { getAuthToken } from 'app/lib/checkAuth';
import {
    CLAIMS_REQUESTING,
    CLAIM_UPDATING,
    CLAIM_CREATING,
    CLAIM_TYPES
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

function* updateFlow({ data, id, reqType }) {
    try {
        const token = yield call(getAuthToken);
        const path = `${id}/${reqType}${
            reqType === 'status' ? '/' + data.status : ''
        }`;
        yield call(api, token, config.claims.update, data, path);
        yield put(actions.claimUpdateSuccess());
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* createFlow({ data }) {
    try {
        const token = yield call(getAuthToken);
        const newClaim = yield call(api, token, config.claims.create, data);
        yield put(actions.claimCreateSuccess(newClaim));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* claimTypesFlow() {
    try {
        const token = yield call(getAuthToken);
        const typesList = yield call(api, token, config.claims.types);
        yield put(actions.claimsTypesSuccess(typesList));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* claimsWatcher() {
    yield [
        takeLatest(CLAIMS_REQUESTING, requestFlow),
        takeLatest(CLAIM_UPDATING, updateFlow),
        takeLatest(CLAIM_CREATING, createFlow),
        takeLatest(CLAIM_TYPES, claimTypesFlow)
    ];
}

export default claimsWatcher;
