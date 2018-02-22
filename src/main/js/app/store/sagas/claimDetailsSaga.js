import { all, call, put, take, takeLatest } from 'redux-saga/effects';
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

function* detailsUpdateFlow(id, data) {
    try {
        const token = yield call(getAuthToken);
        const path = `${id}/data`;

        yield call(
            api,
            token,
            config.claims.updateDetails,
            { ...data, userId: token },
            path
        );
        return data;
    } catch (error) {
        yield put(actions.claimRequestError(error));
    }
}

function* watcher() {
    yield [takeLatest(CLAIM_REQUESTING, requestFlow)];

    const action = yield take(CLAIM_DETAILS_UPDATING);
    const fieldsList = [
        ...action.data.optionalData,
        ...action.data.requiredData
    ];
    const results = yield all(
        fieldsList.map(item => call(detailsUpdateFlow, action.id, item))
    );
    yield put(actions.claimDetailsUpdateSuccess(results));
}

export default watcher;
