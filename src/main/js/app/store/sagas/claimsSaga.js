import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    CLAIMS_REQUESTING,
    CLAIM_UPDATING,
    CLAIM_TYPES,
    CLAIMS_BY_USER
} from '../constants/claims';
import * as actions from '../actions/claimsActions';

function* requestFlow() {
    try {
        const { data } = yield call(api, config.claims.getList);
        yield put(actions.claimsRequestSuccess(data));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* requestByUserFlow({ id }) {
    try {
        const { data } = yield call(
            api,
            config.claims.getListByUserId,
            null,
            id
        );
        yield put(actions.claimsByUserSuccess(data));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* updateFlow({ data, id, reqType }) {
    try {
        const path = `${id}/${reqType}`;
        yield call(api, config.claims.update, { userId: id, ...data }, path);
        yield put(actions.claimUpdateSuccess(reqType, data));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* claimTypesFlow() {
    try {
        const { data } = yield call(api, config.claims.types);
        yield put(actions.claimsTypesSuccess(data));
    } catch (error) {
        yield put(actions.claimsRequestError(error));
    }
}

function* claimsWatcher() {
    yield [
        takeLatest(CLAIMS_REQUESTING, requestFlow),
        takeLatest(CLAIM_UPDATING, updateFlow),
        takeLatest(CLAIM_TYPES, claimTypesFlow),
        takeLatest(CLAIMS_BY_USER, requestByUserFlow)
    ];
}

export default claimsWatcher;
