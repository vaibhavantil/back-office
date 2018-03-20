import { take, fork, cancel, call, put, cancelled } from 'redux-saga/effects';
import { history } from 'app/store';
import api from 'app/api';
import config from 'app/api/config';
import { setClient, unsetClient } from '../actions/clientActions';
import {
    LOGIN_PROCESS,
    LOGIN_SUCCESS,
    LOGIN_ERROR,
    CLIENT_UNSET
} from 'constants/login';

const clearStore = () => {
    unsetClient();
    history.push('/login/oauth');
};

export function* logout() {
    try {
        yield call(api, config.login.logout);
        clearStore();
    } catch (error) {
        clearStore();
    }
}

function* loginFlow() {
    let request;
    try {
        request = yield call(api, config.login.login);
        yield put(setClient(request.data));
        history.push('/dashboard');
        yield put({ type: LOGIN_SUCCESS });
    } catch (error) {
        yield put({ type: LOGIN_ERROR, error });
    } finally {
        if (yield cancelled()) {
            history.push('/login/oauth');
        }
    }

    return request;
}

function* loginWatcher() {
    while (true) {
        yield take(LOGIN_PROCESS);
        const task = yield fork(loginFlow);
        const action = yield take([CLIENT_UNSET, LOGIN_ERROR]);
        if (action.type === CLIENT_UNSET) {
            yield cancel(task);
        }
        yield call(logout);
    }
}

export default loginWatcher;
