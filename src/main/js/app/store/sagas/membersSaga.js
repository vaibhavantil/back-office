import { call, put, takeLatest } from 'redux-saga/effects';
import api from 'app/api';
import config from 'app/api/config';
import {
    MEMBERS_REQUESTING,
    MEMBER_SEARCH_REQUESTING,
    SET_MEMBER_FILTER
} from 'constants/members';
import {
    membersRequestSuccess,
    membersRequestError,
    searchMembersSuccess
} from '../actions/membersActions';
import { showNotification } from '../actions/notificationsActions';

function* membersRequestFlow() {
    try {
        const members = yield call(api, config.members.get);
        yield put(membersRequestSuccess(members.data));
    } catch (error) {
        yield [
            put(membersRequestError(error)),
            put(showNotification({ message: error.message, header: 'Members' }))
        ];
    }
}

function* membersSearchFlow({ query }) {
    try {
        const queryParams = {
            ...query,
            status: query.status === 'ALL' ? '' : query.status
        };
        const searchResult = yield call(
            api,
            config.members.search,
            null,
            '',
            queryParams
        );
        yield put(searchMembersSuccess(searchResult.data));
    } catch (error) {
        yield [
            put(membersRequestError(error)),
            put(showNotification({ message: error.message, header: 'Members' }))
        ];
    }
}

function* membersWatcher() {
    yield [
        takeLatest(MEMBERS_REQUESTING, membersRequestFlow),
        takeLatest(SET_MEMBER_FILTER, membersSearchFlow),
        takeLatest(MEMBER_SEARCH_REQUESTING, membersSearchFlow)
    ];
}

export default membersWatcher;
