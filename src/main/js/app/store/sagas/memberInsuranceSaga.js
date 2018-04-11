import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    MEMBER_INS_REQUESTING,
    MEMBER_INS_SEARCH_REQUESTING
} from '../constants/memberInsurance';

import {
    memberInsRequestSuccess,
    searchMemberInsSuccess,
    memberInsRequestError
} from '../actions/memberInsuranceActions';
import { showNotification } from '../actions/notificationsActions';

function* requestFlow() {
    try {
        const members = yield call(api, config.insMembers.get);
        yield put(memberInsRequestSuccess(members.data));
    } catch (error) {
        yield [
            yield put(memberInsRequestError(error)),
            yield put(
                showNotification({
                    mesage: error.message,
                    header: 'Member insurance'
                })
            )
        ];
    }
}

function* searchFlow({ query }) {
    try {
        const queryParams = {
            ...query,
            status: query.status === 'ALL' ? '' : query.status
        };
        const searchResult = yield call(
            api,
            config.insMembers.search,
            null,
            '',
            queryParams
        );
        yield put(searchMemberInsSuccess(searchResult.data));
    } catch (error) {
        yield [
            put(memberInsRequestError(error)),
            put(showNotification({ message: error.message, header: 'Members' }))
        ];
    }
}

function* membersInsuranceWatcher() {
    yield [
        takeLatest(MEMBER_INS_REQUESTING, requestFlow),
        takeLatest(MEMBER_INS_SEARCH_REQUESTING, searchFlow)
    ];
}

export default membersInsuranceWatcher;
